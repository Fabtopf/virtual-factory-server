package de.cybine.factory.exception.handler;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.cybine.quarkus.api.response.ApiError;
import de.cybine.quarkus.api.response.ApiResponse;
import de.cybine.quarkus.util.api.ApiQueryHelper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

@Slf4j
@SuppressWarnings("unused")
public class ApiConstraintViolationExceptionHandler
{
    @ServerExceptionMapper
    public RestResponse<ApiResponse<ApiError>> toResponse(ConstraintViolationException exception)
    {
        log.debug("A handled exception was thrown during api-request", exception);
        return ApiResponse.<ApiError>builder()
                          .statusCode(RestResponse.Status.BAD_REQUEST.getStatusCode())
                          .error(ApiError.builder()
                                         .code("api-constraint-violation")
                                         .message("invalid request data provided")
                                         .data("violations", exception.getConstraintViolations()
                                                                      .stream()
                                                                      .map(this::createViolation)
                                                                      .toList())
                                         .build())
                          .build()
                          .transform(ApiQueryHelper::createResponse);
    }

    private Violation createViolation(ConstraintViolation<?> violation)
    {
        return Violation.builder()
                        .property(violation.getPropertyPath().toString())
                        .description(violation.getMessage())
                        .build();
    }

    @Data
    @Jacksonized
    @Builder(builderClassName = "Generator")
    public static class Violation
    {
        @JsonProperty("property")
        private final String property;

        @JsonProperty("description")
        private final String description;
    }
}
