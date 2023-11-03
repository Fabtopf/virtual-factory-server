package de.cybine.factory.exception.handler;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.cybine.factory.util.api.response.ApiResponse;
import de.cybine.factory.util.api.response.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

@SuppressWarnings("unused")
public class ApiConstraintViolationExceptionHandler
{
    @ServerExceptionMapper
    public RestResponse<ApiResponse<ErrorResponse>> toResponse(ConstraintViolationException exception)
    {
        return ApiResponse.<ErrorResponse>builder()
                          .status(RestResponse.Status.BAD_REQUEST)
                          .value(ErrorResponse.builder()
                                              .code("api-constraint-violation")
                                              .message("invalid request data provided")
                                              .data("violations", exception.getConstraintViolations()
                                                                           .stream()
                                                                           .map(this::createViolation)
                                                                           .toList())
                                              .build())
                          .build()
                          .toResponse();
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
