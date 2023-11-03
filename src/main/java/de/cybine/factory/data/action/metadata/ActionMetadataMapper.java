package de.cybine.factory.data.action.metadata;

import de.cybine.factory.data.action.context.ActionContext;
import de.cybine.factory.data.action.context.ActionContextEntity;
import de.cybine.factory.util.converter.ConversionHelper;
import de.cybine.factory.util.converter.EntityMapper;
import de.cybine.factory.util.datasource.WithDatasourceKey;

import java.util.Optional;

public class ActionMetadataMapper implements EntityMapper<ActionMetadataEntity, ActionMetadata>
{
    @Override
    public Class<ActionMetadataEntity> getEntityType( )
    {
        return ActionMetadataEntity.class;
    }

    @Override
    public Class<ActionMetadata> getDataType( )
    {
        return ActionMetadata.class;
    }

    @Override
    public ActionMetadataEntity toEntity(ActionMetadata data, ConversionHelper helper)
    {
        return ActionMetadataEntity.builder()
                                   .id(data.findId().map(ActionMetadataId::getValue).orElse(null))
                                   .namespace(data.getNamespace())
                                   .category(data.getCategory())
                                   .name(data.getName())
                                   .type(helper.optional(data::getType)
                                               .map(WithDatasourceKey::getDatasourceKey)
                                               .orElse(null))
                                   .contexts(helper.toSet(ActionContext.class, ActionContextEntity.class)
                                                   .map(data::getContexts))
                                   .build();
    }

    @Override
    public ActionMetadata toData(ActionMetadataEntity entity, ConversionHelper helper)
    {
        return ActionMetadata.builder()
                             .id(helper.optional(entity::getId).map(ActionMetadataId::of).orElse(null))
                             .namespace(entity.getNamespace())
                             .category(entity.getCategory())
                             .name(entity.getName())
                             .type(helper.optional(entity::getType)
                                         .map(ActionType::findByName)
                                         .map(Optional::orElseThrow)
                                         .orElse(null))
                             .contexts(helper.toSet(ActionContextEntity.class, ActionContext.class)
                                             .map(entity::getContexts))
                             .build();
    }
}