package de.cybine.factory.exception.handler;

import de.cybine.factory.exception.ServiceException;
import de.cybine.factory.util.api.response.ApiResponse;
import de.cybine.factory.util.api.response.ErrorResponse;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

@SuppressWarnings("unused")
public class ServiceExceptionHandler
{
    @ServerExceptionMapper
    public RestResponse<ApiResponse<ErrorResponse>> toResponse(ServiceException exception)
    {
        return ApiResponse.<ErrorResponse>builder()
                          .status(exception.getStatus())
                          .value(exception.toResponse())
                          .build()
                          .toResponse();
    }
}
