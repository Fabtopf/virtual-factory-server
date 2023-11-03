package de.cybine.factory.data.action.context;

import de.cybine.factory.data.action.metadata.ActionMetadata;
import de.cybine.factory.data.action.metadata.ActionMetadataEntity;
import de.cybine.factory.data.action.metadata.ActionMetadataId;
import de.cybine.factory.data.action.process.ActionProcess;
import de.cybine.factory.data.action.process.ActionProcessEntity;
import de.cybine.factory.data.util.primitive.Id;
import de.cybine.factory.util.converter.ConversionHelper;
import de.cybine.factory.util.converter.EntityMapper;

public class ActionContextMapper implements EntityMapper<ActionContextEntity, ActionContext>
{
    @Override
    public Class<ActionContextEntity> getEntityType( )
    {
        return ActionContextEntity.class;
    }

    @Override
    public Class<ActionContext> getDataType( )
    {
        return ActionContext.class;
    }

    @Override
    public ActionContextEntity toEntity(ActionContext data, ConversionHelper helper)
    {
        return ActionContextEntity.builder()
                                  .id(data.findId().map(Id::getValue).orElse(null))
                                  .metadataId(helper.optional(data::getMetadataId).map(Id::getValue).orElse(null))
                                  .metadata(helper.toItem(ActionMetadata.class, ActionMetadataEntity.class)
                                                  .map(data::getMetadata))
                                  .correlationId(data.getCorrelationId())
                                  .itemId(data.getItemId().orElse(null))
                                  .processes(helper.toSet(ActionProcess.class, ActionProcessEntity.class)
                                                   .map(data::getProcesses))
                                  .build();
    }

    @Override
    public ActionContext toData(ActionContextEntity entity, ConversionHelper helper)
    {
        return ActionContext.builder()
                            .id(ActionContextId.of(entity.getId()))
                            .metadataId(helper.optional(entity::getMetadataId).map(ActionMetadataId::of).orElse(null))
                            .metadata(helper.toItem(ActionMetadataEntity.class, ActionMetadata.class)
                                            .map(entity::getMetadata))
                            .correlationId(entity.getCorrelationId())
                            .itemId(entity.getItemId().orElse(null))
                            .processes(helper.toSet(ActionProcessEntity.class, ActionProcess.class)
                                             .map(entity::getProcesses))
                            .build();
    }
}
