package de.cybine.factory.exception.converter;

import de.cybine.factory.exception.ServiceException;
import org.jboss.resteasy.reactive.RestResponse;

@SuppressWarnings("unused")
public class EntityConversionException extends ServiceException
{
    public EntityConversionException(String message)
    {
        this(message, null);
    }

    public EntityConversionException(Throwable cause)
    {
        this(null, cause);
    }

    public EntityConversionException(String message, Throwable cause)
    {
        super("entity-conversion-error", RestResponse.Status.INTERNAL_SERVER_ERROR, message, cause);
    }
}
