package de.cybine.factory.exception.handler;

import de.cybine.quarkus.api.response.ApiError;
import de.cybine.quarkus.util.api.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

@Slf4j
@SuppressWarnings("unused")
public class ConstraintViolationExceptionHandler
{
    @ServerExceptionMapper
    public RestResponse<ApiResponse<ApiError>> toResponse(ConstraintViolationException exception)
    {
        log.debug("A handled exception was thrown during api-request", exception);
        return ApiResponse.<ApiError>builder()
                          .status(RestResponse.Status.CONFLICT)
                          .error(ApiError.builder()
                                         .code("db-constraint-violation")
                                         .message(exception.getCause().getMessage())
                                         .build())
                          .build()
                          .toResponse();
    }
}
