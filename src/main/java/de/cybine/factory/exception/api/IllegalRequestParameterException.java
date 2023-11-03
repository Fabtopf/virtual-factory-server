package de.cybine.factory.exception.api;

import de.cybine.factory.exception.ServiceException;
import org.jboss.resteasy.reactive.RestResponse;

@SuppressWarnings("unused")
public class IllegalRequestParameterException extends ServiceException
{
    public IllegalRequestParameterException(String message)
    {
        this(message, null);
    }

    public IllegalRequestParameterException(String message, Throwable cause)
    {
        super("illegal-request-parameter", RestResponse.Status.BAD_REQUEST, message, cause);
    }
}
