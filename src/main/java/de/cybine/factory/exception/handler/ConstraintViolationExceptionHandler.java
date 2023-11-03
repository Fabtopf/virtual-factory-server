package de.cybine.factory.exception.handler;

import de.cybine.factory.util.api.response.ApiResponse;
import de.cybine.factory.util.api.response.ErrorResponse;
import org.hibernate.exception.ConstraintViolationException;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

@SuppressWarnings("unused")
public class ConstraintViolationExceptionHandler
{
    @ServerExceptionMapper
    public RestResponse<ApiResponse<ErrorResponse>> toResponse(ConstraintViolationException exception)
    {
        return ApiResponse.<ErrorResponse>builder()
                          .status(RestResponse.Status.CONFLICT)
                          .value(ErrorResponse.builder()
                                              .code("db-constraint-violation")
                                              .message(exception.getCause().getMessage())
                                              .build())
                          .build()
                          .toResponse();
    }
}
