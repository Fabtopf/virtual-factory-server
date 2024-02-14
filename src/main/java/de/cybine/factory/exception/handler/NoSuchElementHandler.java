package de.cybine.factory.exception.handler;

import de.cybine.quarkus.api.response.ApiError;
import de.cybine.quarkus.util.api.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import java.util.NoSuchElementException;

@Slf4j
@SuppressWarnings("unused")
public class NoSuchElementHandler
{
    @ServerExceptionMapper
    public RestResponse<ApiResponse<ApiError>> toResponse(NoSuchElementException exception)
    {
        log.debug("A handled exception was thrown during api-request", exception);
        return ApiResponse.<ApiError>builder()
                          .status(RestResponse.Status.NOT_FOUND)
                          .error(ApiError.builder().code("element-not-found").message(exception.getMessage()).build())
                          .build()
                          .toResponse();
    }
}
