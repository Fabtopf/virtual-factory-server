package de.cybine.factory.data.action.process;

import de.cybine.factory.data.action.context.ActionContext;
import de.cybine.factory.data.action.context.ActionContextEntity;
import de.cybine.factory.data.action.context.ActionContextId;
import de.cybine.quarkus.data.util.primitive.Id;
import de.cybine.quarkus.util.converter.ConversionHelper;
import de.cybine.quarkus.util.converter.EntityMapper;

public class ActionProcessMapper implements EntityMapper<ActionProcessEntity, ActionProcess>
{
    @Override
    public Class<ActionProcessEntity> getEntityType( )
    {
        return ActionProcessEntity.class;
    }

    @Override
    public Class<ActionProcess> getDataType( )
    {
        return ActionProcess.class;
    }

    @Override
    public ActionProcessEntity toEntity(ActionProcess data, ConversionHelper helper)
    {
        return ActionProcessEntity.builder()
                                  .id(data.findId().map(Id::getValue).orElse(null))
                                  .eventId(data.getEventId())
                                  .contextId(helper.optional(data::getContextId).map(Id::getValue).orElse(null))
                                  .context(helper.toItem(ActionContext.class, ActionContextEntity.class)
                                                 .map(data::getContext))
                                  .status(data.getStatus())
                                  .priority(data.getPriority().orElse(100))
                                  .description(data.getDescription().orElse(null))
                                  .creatorId(data.getCreatorId().orElse(null))
                                  .createdAt(data.getCreatedAt())
                                  .dueAt(data.getDueAt().orElse(null))
                                  .data(data.getData().orElse(null))
                                  .build();
    }

    @Override
    public ActionProcess toData(ActionProcessEntity entity, ConversionHelper helper)
    {
        return ActionProcess.builder()
                            .id(helper.optional(entity::getId).map(ActionProcessId::of).orElse(null))
                            .eventId(entity.getEventId())
                            .contextId(helper.optional(entity::getContextId).map(ActionContextId::of).orElse(null))
                            .context(helper.toItem(ActionContextEntity.class, ActionContext.class)
                                           .map(entity::getContext))
                            .status(entity.getStatus())
                            .priority(entity.getPriority())
                            .description(entity.getDescription().orElse(null))
                            .createdAt(entity.getCreatedAt())
                            .dueAt(entity.getDueAt().orElse(null))
                            .data(entity.getData().orElse(null))
                            .build();
    }
}
