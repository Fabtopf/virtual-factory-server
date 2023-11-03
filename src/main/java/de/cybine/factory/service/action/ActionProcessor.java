package de.cybine.factory.service.action;

import de.cybine.factory.data.action.process.ActionProcess;
import de.cybine.factory.data.action.process.ActionProcessMetadata;

public interface ActionProcessor<T>
{
    ActionProcessorMetadata getMetadata( );

    boolean shouldExecute(ActionService service, ActionProcessMetadata nextState);

    ActionProcessorResult<T> process(ActionService service, ActionProcess previousState);
}
