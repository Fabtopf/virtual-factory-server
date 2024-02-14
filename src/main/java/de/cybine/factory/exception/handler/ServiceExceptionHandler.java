package de.cybine.factory.exception.handler;

import de.cybine.quarkus.api.response.ApiError;
import de.cybine.quarkus.exception.ServiceException;
import de.cybine.quarkus.util.api.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.RestResponse.Status;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

@Slf4j
@SuppressWarnings("unused")
public class ServiceExceptionHandler
{
    @ServerExceptionMapper
    public RestResponse<ApiResponse<ApiError>> toResponse(ServiceException exception)
    {
        log.debug("A handled exception was thrown during api-request", exception);
        return ApiResponse.<ApiError>builder()
                          .status(Status.fromStatusCode(exception.getStatusCode()))
                          .error(exception.toResponse())
                          .build()
                          .toResponse();
    }
}
