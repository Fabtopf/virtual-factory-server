package de.cybine.factory.exception.action;

import de.cybine.factory.exception.ServiceException;
import org.jboss.resteasy.reactive.RestResponse;

@SuppressWarnings("unused")
public class UnknownActionException extends ServiceException
{
    public UnknownActionException(String message)
    {
        this(message, null);
    }

    public UnknownActionException(String message, Throwable cause)
    {
        super("unknown-action", RestResponse.Status.BAD_REQUEST, message, cause);
    }
}
