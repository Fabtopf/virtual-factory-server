package de.cybine.factory.service.action;

import de.cybine.factory.data.action.context.ActionContext;
import de.cybine.factory.data.action.context.ActionContextEntity;
import de.cybine.factory.data.action.context.ActionContextId;
import de.cybine.factory.data.action.process.ActionProcess;
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
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static de.cybine.factory.data.action.context.ActionContextEntity_.*;

@Startup
@Singleton
@RequiredArgsConstructor
public class ContextService
{
    private final GenericApiQueryService<ActionContextEntity, ActionContext> service = GenericApiQueryService.forType(
            ActionContextEntity.class, ActionContext.class);

    private final ApiFieldResolver resolver;

    @PostConstruct
    void setup( )
    {
        this.resolver.registerType(ActionContext.class)
                     .withField("id", ID)
                     .withField("namespace", NAMESPACE)
                     .withField("category", CATEGORY)
                     .withField("name", NAME)
                     .withField("correlation_id", CORRELATION_ID)
                     .withField("item_id", ITEM_ID)
                     .withRelation("processes", PROCESSES, ActionProcess.class);
    }

    public Optional<ActionContext> fetchById(ActionContextId id)
    {
        DatasourceConditionDetail<UUID> idEquals = DatasourceHelper.isEqual(ID, id.getValue());
        DatasourceConditionInfo condition = DatasourceHelper.and(idEquals);

        return this.service.fetchSingle(DatasourceQuery.builder().condition(condition).build());
    }

    public Optional<ActionContext> fetchByCorrelationId(String correlationId)
    {
        DatasourceConditionDetail<String> correlationIdEquals = DatasourceHelper.isEqual(CORRELATION_ID, correlationId);
        DatasourceConditionInfo condition = DatasourceHelper.and(correlationIdEquals);

        return this.service.fetchSingle(DatasourceQuery.builder().condition(condition).build());
    }

    public List<ActionContext> fetch(ApiQuery query)
    {
        return this.service.fetch(query);
    }

    public Optional<ActionContext> fetchSingle(ApiQuery query)
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
