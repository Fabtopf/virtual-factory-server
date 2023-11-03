package de.cybine.factory.exception.handler;

import de.cybine.factory.util.api.response.ApiResponse;
import de.cybine.factory.util.api.response.ErrorResponse;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import java.util.NoSuchElementException;

@SuppressWarnings("unused")
public class NoSuchElementHandler
{
    @ServerExceptionMapper
    public RestResponse<ApiResponse<ErrorResponse>> toResponse(NoSuchElementException exception)
    {
        exception.printStackTrace();
        return ApiResponse.<ErrorResponse>builder()
                          .status(RestResponse.Status.NOT_FOUND)
                          .value(ErrorResponse.builder()
                                              .code("element-not-found")
                                              .message(exception.getMessage())
                                              .build())
                          .build()
                          .toResponse();
    }
}
