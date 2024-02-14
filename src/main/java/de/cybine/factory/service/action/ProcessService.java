package de.cybine.factory.service.action;

import de.cybine.factory.data.action.context.ActionContextEntity_;
import de.cybine.factory.data.action.process.ActionProcess;
import de.cybine.factory.data.action.process.ActionProcessEntity;
import de.cybine.factory.data.action.process.ActionProcessId;
import de.cybine.quarkus.util.api.ApiFieldResolver;
import de.cybine.quarkus.util.api.GenericApiQueryService;
import de.cybine.quarkus.util.api.query.ApiCountInfo;
import de.cybine.quarkus.util.api.query.ApiCountQuery;
import de.cybine.quarkus.util.api.query.ApiOptionQuery;
import de.cybine.quarkus.util.api.query.ApiQuery;
import de.cybine.quarkus.util.cloudevent.CloudEvent;
import de.cybine.quarkus.util.converter.ConversionResult;
import de.cybine.quarkus.util.converter.ConverterRegistry;
import de.cybine.quarkus.util.datasource.*;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static de.cybine.factory.data.action.process.ActionProcessEntity_.*;

@Startup
@Singleton
@RequiredArgsConstructor
public class ProcessService
{
    private final GenericApiQueryService<ActionProcessEntity, ActionProcess> service = GenericApiQueryService.forType(
            ActionProcessEntity.class, ActionProcess.class);

    private final ApiFieldResolver  resolver;
    private final ConverterRegistry converterRegistry;

    @PostConstruct
    void setup( )
    {
        this.resolver.registerType(ActionProcess.class)
                     .withField("id", ID)
                     .withField("event_id", EVENT_ID)
                     .withField("context_id", CONTEXT_ID)
                     .withField("context", CONTEXT)
                     .withField("status", STATUS)
                     .withField("priority", PRIORITY)
                     .withField("description", DESCRIPTION)
                     .withField("creator_id", CREATOR_ID)
                     .withField("created_at", CREATED_AT)
                     .withField("due_at", DUE_AT)
                     .withField("data", DATA);
    }

    public Optional<ActionProcess> fetchById(ActionProcessId id)
    {
        DatasourceConditionDetail<UUID> idEquals = DatasourceHelper.isEqual(ID, id.getValue());
        DatasourceConditionInfo condition = DatasourceHelper.and(idEquals);

        return this.service.fetchSingle(DatasourceQuery.builder().condition(condition).build());
    }

    public Optional<ActionProcess> fetchByEventId(String eventId)
    {
        DatasourceConditionDetail<String> idEquals = DatasourceHelper.isEqual(EVENT_ID, eventId);
        DatasourceConditionInfo condition = DatasourceHelper.and(idEquals);

        return this.service.fetchSingle(DatasourceQuery.builder().condition(condition).build());
    }

    public List<ActionProcess> fetchByCorrelationId(String correlationId)
    {
        DatasourceConditionDetail<String> correlationIdEquals = DatasourceHelper.isEqual(
                ActionContextEntity_.CORRELATION_ID, correlationId);
        DatasourceConditionInfo condition = DatasourceHelper.and(correlationIdEquals);

        DatasourceRelationInfo contextRelation = DatasourceRelationInfo.builder()
                                                                       .property(CONTEXT.getName())
                                                                       .condition(condition)
                                                                       .build();

        return this.service.fetch(DatasourceQuery.builder().relation(contextRelation).build());
    }

    public List<ActionProcess> fetch(ApiQuery query)
    {
        return this.service.fetch(query);
    }

    public Optional<ActionProcess> fetchSingle(ApiQuery query)
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

    public Optional<CloudEvent> fetchAsCloudEventByEventId(String eventId)
    {
        DatasourceConditionDetail<String> idEquals = DatasourceHelper.isEqual(EVENT_ID, eventId);
        DatasourceConditionInfo condition = DatasourceHelper.and(idEquals);

        DatasourceRelationInfo contextRelation = DatasourceRelationInfo.builder()
                                                                       .property(CONTEXT.getName())
                                                                       .fetch(true)
                                                                       .build();

        DatasourceQuery query = DatasourceQuery.builder().condition(condition).relation(contextRelation).build();
        return this.service.fetchSingle(query)
                           .map(this.converterRegistry.getProcessor(ActionProcess.class, CloudEvent.class)::toItem)
                           .map(ConversionResult::result);
    }

    public List<CloudEvent> fetchAsCloudEventsByCorrelationId(String correlationId)
    {
        DatasourceConditionDetail<String> idEquals = DatasourceHelper.isEqual(ActionContextEntity_.CORRELATION_ID,
                correlationId);
        DatasourceConditionInfo condition = DatasourceHelper.and(idEquals);

        DatasourceRelationInfo contextRelation = DatasourceRelationInfo.builder()
                                                                       .property(CONTEXT.getName())
                                                                       .fetch(true)
                                                                       .condition(condition)
                                                                       .build();

        DatasourceQuery query = DatasourceQuery.builder().relation(contextRelation).build();
        return this.converterRegistry.getProcessor(ActionProcess.class, CloudEvent.class)
                                     .toList(this.service.fetch(query))
                                     .result();
    }
}
