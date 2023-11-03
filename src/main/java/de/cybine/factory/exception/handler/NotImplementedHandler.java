package de.cybine.factory.exception.handler;

import de.cybine.factory.exception.api.NotImplementedException;
import de.cybine.factory.util.api.response.ApiResourceInfo;
import de.cybine.factory.util.api.response.ApiResponse;
import de.cybine.factory.util.api.response.ErrorResponse;
import io.vertx.core.http.HttpServerRequest;
import jakarta.ws.rs.core.Context;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

@SuppressWarnings("unused")
public class NotImplementedHandler
{
    @Context
    HttpServerRequest request;

    @ServerExceptionMapper
    public RestResponse<ApiResponse<ErrorResponse>> toResponse(NotImplementedException exception)
    {
        return ApiResponse.<ErrorResponse>builder()
                          .status(RestResponse.Status.NOT_IMPLEMENTED)
                          .self(ApiResourceInfo.builder().href(this.request.absoluteURI()).build())
                          .value(ErrorResponse.builder().message(exception.getMessage()).build())
                          .build()
                          .toResponse();
    }
}
