package de.cybine.factory.exception.action;

import de.cybine.factory.exception.ServiceException;
import org.jboss.resteasy.reactive.RestResponse;

@SuppressWarnings("unused")
public class ActionPreconditionException extends ServiceException
{
    public ActionPreconditionException(String message)
    {
        this(message, null);
    }

    public ActionPreconditionException(String message, Throwable cause)
    {
        super("action-precondition-unfulfilled", RestResponse.Status.BAD_REQUEST, message, cause);
    }
}
