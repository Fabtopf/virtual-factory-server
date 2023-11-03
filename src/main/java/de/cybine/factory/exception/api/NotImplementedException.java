package de.cybine.factory.exception.api;

import de.cybine.factory.exception.ServiceException;
import org.jboss.resteasy.reactive.RestResponse;

@SuppressWarnings("unused")
public class NotImplementedException extends ServiceException
{
    public NotImplementedException( )
    {
        super("not-implemented", RestResponse.Status.NOT_IMPLEMENTED, "This method has not been implemented yet.");
    }
}
