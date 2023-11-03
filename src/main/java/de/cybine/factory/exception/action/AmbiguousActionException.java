package de.cybine.factory.exception.action;

import de.cybine.factory.exception.ServiceException;
import org.jboss.resteasy.reactive.RestResponse;

@SuppressWarnings("unused")
public class AmbiguousActionException extends ServiceException
{
    public AmbiguousActionException(String message)
    {
        this(message, null);
    }

    public AmbiguousActionException(String message, Throwable cause)
    {
        super("ambiguous-action", RestResponse.Status.CONFLICT, message, cause);
    }
}
