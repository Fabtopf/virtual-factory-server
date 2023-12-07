package de.cybine.factory.service.sensor;

import de.cybine.factory.data.action.context.ActionContextMetadata;
import de.cybine.factory.data.sensor.Sensor;
import de.cybine.factory.data.sensor.SensorEntity;
import de.cybine.factory.data.sensor.SensorId;
import de.cybine.factory.service.action.ActionProcessorRegistry;
import de.cybine.factory.service.action.BaseActionProcessStatus;
import de.cybine.factory.service.action.GenericActionProcessor;
import de.cybine.factory.util.api.ApiFieldResolver;
import de.cybine.factory.util.api.query.ApiCountInfo;
import de.cybine.factory.util.api.query.ApiCountQuery;
import de.cybine.factory.util.api.query.ApiOptionQuery;
import de.cybine.factory.util.api.query.ApiQuery;
import de.cybine.factory.util.datasource.*;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static de.cybine.factory.data.sensor.SensorEntity_.*;

@Startup
@ApplicationScoped
@RequiredArgsConstructor
public class SensorService
{
    public static final ActionContextMetadata LOG_EVENT = ActionContextMetadata.builder()
                                                                               .namespace("virtual-factory")
                                                                               .category("sensor")
                                                                               .name("event")
                                                                               .build();

    private final GenericDatasourceService<SensorEntity, Sensor> service = GenericDatasourceService.forType(
            SensorEntity.class, Sensor.class);

    private final ApiFieldResolver        resolver;
    private final ActionProcessorRegistry actionRegistry;

    @PostConstruct
    void setup( )
    {
        this.resolver.registerTypeRepresentation(Sensor.class, SensorEntity.class)
                     .registerField("id", ID)
                     .registerField("reference_id", REFERENCE_ID)
                     .registerField("type", TYPE)
                     .registerField("description", DESCRIPTION);

        String logEventState = "log-event";
        String initialized = BaseActionProcessStatus.INITIALIZED.getName();
        String terminated = BaseActionProcessStatus.TERMINATED.getName();

        this.actionRegistry.registerProcessor(
                GenericActionProcessor.of(LOG_EVENT.toProcessorMetadata(initialized, logEventState)));
        this.actionRegistry.registerProcessor(
                GenericActionProcessor.of(LOG_EVENT.toProcessorMetadata(logEventState, logEventState)));
        this.actionRegistry.registerProcessor(
                GenericActionProcessor.of(LOG_EVENT.toProcessorMetadata(logEventState, terminated)));
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
