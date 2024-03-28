package de.cybine.factory.service.sensor;

import de.cybine.factory.config.ApplicationConfig;
import de.cybine.factory.data.action.context.ActionContext;
import de.cybine.factory.data.sensor.Sensor;
import de.cybine.factory.data.sensor.SensorEntity;
import de.cybine.factory.data.sensor.SensorId;
import de.cybine.factory.service.action.ActionService;
import de.cybine.quarkus.util.action.data.Action;
import de.cybine.quarkus.util.action.data.ActionData;
import de.cybine.quarkus.util.action.data.ActionMetadata;
import de.cybine.quarkus.util.action.stateful.WorkflowBuilder;
import de.cybine.quarkus.util.action.stateful.WorkflowType;
import de.cybine.quarkus.util.api.ApiFieldResolver;
import de.cybine.quarkus.util.api.GenericApiQueryService;
import de.cybine.quarkus.util.api.query.ApiCountInfo;
import de.cybine.quarkus.util.api.query.ApiCountQuery;
import de.cybine.quarkus.util.api.query.ApiOptionQuery;
import de.cybine.quarkus.util.api.query.ApiQuery;
import de.cybine.quarkus.util.datasource.DatasourceConditionDetail;
import de.cybine.quarkus.util.datasource.DatasourceConditionInfo;
import de.cybine.quarkus.util.datasource.DatasourceHelper;
import de.cybine.quarkus.util.datasource.DatasourceQuery;
import de.cybine.quarkus.util.event.EventManager;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

import static de.cybine.factory.data.sensor.SensorEntity_.*;
import static de.cybine.factory.service.action.ActionService.TERMINATED_STATE;
import static de.cybine.quarkus.util.action.ActionProcessorBuilder.on;
import static de.cybine.quarkus.util.action.data.ActionProcessorMetadata.ANY;
import static de.cybine.quarkus.util.action.stateful.Workflow.INITIAL_STATE;

@Slf4j
@Startup
@ApplicationScoped
@RequiredArgsConstructor
public class SensorService
{
    public static final String SENSOR_EVENT_NAMESPACE = "virtual-factory";
    public static final String SENSOR_EVENT_CATEGORY  = "sensor";
    public static final String SENSOR_EVENT_NAME      = "event";

    public static final String LOG_EVENT_ACTION = "log-event";

    private final GenericApiQueryService<SensorEntity, Sensor> service = GenericApiQueryService.forType(
            SensorEntity.class, Sensor.class);

    private final ApiFieldResolver resolver;
    private final ActionService    actionService;
    private final EventManager     eventManager;

    private final ApplicationConfig appConfig;

    @PostConstruct
    void setup( )
    {
        this.resolver.registerType(Sensor.class)
                     .withField("id", ID)
                     .withField("reference_id", REFERENCE_ID)
                     .withField("type", TYPE)
                     .withField("description", DESCRIPTION);

        WorkflowBuilder.create(SENSOR_EVENT_NAMESPACE, SENSOR_EVENT_CATEGORY, SENSOR_EVENT_NAME)
                       .initialState(INITIAL_STATE)
                       .type(WorkflowType.LOG)
                       .with(on(LOG_EVENT_ACTION).from(ANY))
                       .with(on(TERMINATED_STATE).from(ANY))
                       .apply(this.actionService);
    }

    public Optional<Sensor> fetchById(SensorId id)
    {
        DatasourceConditionDetail<Long> idEquals = DatasourceHelper.isEqual(ID, id.getValue());
        DatasourceConditionInfo condition = DatasourceHelper.and(idEquals);

        return this.service.fetchSingle(DatasourceQuery.builder().condition(condition).build());
    }

    public Optional<Sensor> fetchByReferenceId(String referenceId)
    {
        DatasourceConditionDetail<String> idEquals = DatasourceHelper.isEqual(REFERENCE_ID, referenceId);
        DatasourceConditionInfo condition = DatasourceHelper.and(idEquals);

        return this.service.fetchSingle(DatasourceQuery.builder().condition(condition).build());
    }

    public void processEvent(SensorEventData eventData)
    {
        log.debug("New event from {} ({}): {}", eventData.getReferenceId(), eventData.getAction(), eventData.getData());

        String correlationId = this.findActiveRecordingId(eventData.getReferenceId()).orElse(null);
        if (correlationId != null)
        {
            ActionMetadata metadata = this.getMetadataBuilder()
                                          .correlationId(correlationId)
                                          .action(LOG_EVENT_ACTION)
                                          .build();

            this.actionService.perform(Action.of(metadata, ActionData.of(eventData)));
        }

        SensorEvent event = new SensorEvent(eventData);
        this.eventManager.handle(event).await().indefinitely();
    }

    public String startEventRecording(String sensorReference)
    {
        if (sensorReference == null)
            throw new IllegalArgumentException("Sensor reference must not be null");

        String correlationId = this.findActiveRecordingId(sensorReference).orElse(null);
        if (correlationId != null)
            return correlationId;

        return this.actionService.beginWorkflow(SENSOR_EVENT_NAMESPACE, SENSOR_EVENT_CATEGORY, SENSOR_EVENT_NAME,
                sensorReference);
    }

    public void stopEventRecording(String sensorReference)
    {
        if (sensorReference == null)
            throw new IllegalArgumentException("Sensor reference must not be null");

        String correlationId = this.findActiveRecordingId(sensorReference).orElse(null);
        if (correlationId == null)
            return;

        ActionMetadata metadata = this.getMetadataBuilder()
                                      .correlationId(correlationId)
                                      .action(TERMINATED_STATE)
                                      .build();

        this.actionService.perform(Action.of(metadata, null));
    }

    public List<Sensor> fetch(ApiQuery query)
    {
        return this.service.fetch(query);
    }

    public Optional<Sensor> fetchSingle(ApiQuery query)
    {
        return this.service.fetchSingle(query);
    }

    public <O> List<O> fetchOptions(ApiOptionQuery query)
    {
        return this.service.fetchOptions(query);
    }

    public List<ApiCountInfo> fetchTotal(ApiCountQuery query)
    {
        return this.service.fetchTotal(query);
    }

    private Optional<String> findActiveRecordingId(String sensorReference)
    {
        assert sensorReference != null : "No sensor reference present";

        return this.actionService.fetchActiveContexts(SENSOR_EVENT_NAMESPACE, SENSOR_EVENT_CATEGORY, SENSOR_EVENT_NAME,
                sensorReference).stream().map(ActionContext::getCorrelationId).findAny();
    }

    private ActionMetadata.Generator getMetadataBuilder( )
    {
        return ActionMetadata.builder()
                             .namespace(SENSOR_EVENT_NAMESPACE)
                             .category(SENSOR_EVENT_CATEGORY)
                             .name(SENSOR_EVENT_NAME)
                             .source(this.appConfig.appId());
    }
}
