package de.cybine.factory.exception.api;

import de.cybine.factory.exception.ServiceException;
import org.jboss.resteasy.reactive.RestResponse;

@SuppressWarnings("unused")
public class UnknownApiContextException extends ServiceException
{
    public UnknownApiContextException(String message)
    {
        this(message, null);
    }

    public UnknownApiContextException(String message, Throwable cause)
    {
        super("unknown-api-context", RestResponse.Status.BAD_REQUEST, message, cause);
    }
}
