package de.cybine.factory.util.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Map;

@Data
@Builder(builderClassName = "Generator")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse
{
    @JsonProperty("message")
    private final String message;

    @JsonProperty("code")
    private final String code;

    @Singular("data")
    @JsonProperty("data")
    private final Map<String, Object> data;
}
