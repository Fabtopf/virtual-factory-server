package de.cybine.factory.service.action;

import de.cybine.factory.data.action.metadata.ActionMetadata;
import de.cybine.factory.data.action.metadata.ActionMetadataEntity;
import de.cybine.factory.data.action.metadata.ActionMetadataId;
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

import static de.cybine.factory.data.action.metadata.ActionMetadataEntity_.*;

@Startup
@ApplicationScoped
@RequiredArgsConstructor
public class MetadataService
{
    private final GenericDatasourceService<ActionMetadataEntity, ActionMetadata> service =
            GenericDatasourceService.forType(
            ActionMetadataEntity.class, ActionMetadata.class);

    private final ApiFieldResolver resolver;

    @PostConstruct
    void setup( )
    {
        this.resolver.registerTypeRepresentation(ActionMetadata.class, ActionMetadataEntity.class)
                     .registerField("id", ID)
                     .registerField("namespace", NAMESPACE)
                     .registerField("category", CATEGORY)
                     .registerField("name", NAME)
                     .registerField("type", TYPE)
                     .registerField("contexts", CONTEXTS);
    }

    public Optional<ActionMetadata> fetchById(ActionMetadataId id)
    {
        DatasourceConditionDetail<Long> idEquals = DatasourceHelper.isEqual(ID, id.getValue());
        DatasourceConditionInfo condition = DatasourceHelper.and(idEquals);

        return this.service.fetchSingle(DatasourceQuery.builder().condition(condition).build());
    }

    public List<ActionMetadata> fetch(ApiQuery query)
    {
        return this.service.fetch(query);
    }

    public Optional<ActionMetadata> fetchSingle(ApiQuery query)
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
