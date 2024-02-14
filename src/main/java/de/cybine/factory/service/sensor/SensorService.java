package de.cybine.factory.service.sensor;

import de.cybine.factory.data.sensor.Sensor;
import de.cybine.factory.data.sensor.SensorEntity;
import de.cybine.factory.data.sensor.SensorId;
import de.cybine.factory.service.action.ActionService;
import de.cybine.quarkus.util.action.data.ActionProcessorBuilder;
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
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static de.cybine.factory.data.sensor.SensorEntity_.*;
import static de.cybine.factory.service.action.ActionService.TERMINATED_STATE;
import static de.cybine.quarkus.util.action.data.ActionProcessorMetadata.ANY;

@Startup
@ApplicationScoped
@RequiredArgsConstructor
public class SensorService
{
    public static final String CREATION_WORKFLOW = "virtual-factory:sensor:event";

    private final GenericApiQueryService<SensorEntity, Sensor> service = GenericApiQueryService.forType(
            SensorEntity.class, Sensor.class);

    private final ApiFieldResolver resolver;
    private final ActionService    actionService;

    @PostConstruct
    void setup( )
    {
        this.resolver.registerType(Sensor.class)
                     .withField("id", ID)
                     .withField("reference_id", REFERENCE_ID)
                     .withField("type", TYPE)
                     .withField("description", DESCRIPTION);

        WorkflowBuilder.create(CREATION_WORKFLOW)
                       .initialState("initialized")
                       .type(WorkflowType.LOG)
                       .with(ActionProcessorBuilder.on("log-event").from(ANY))
                       .with(ActionProcessorBuilder.on(TERMINATED_STATE).from(ANY))
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
}
