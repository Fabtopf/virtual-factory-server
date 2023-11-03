package de.cybine.factory.util.api.permission;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Jacksonized
@Builder(builderClassName = "Generator")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiContextConfig
{
    @NotNull
    @NotBlank
    @JsonProperty("name")
    private final String name;

    @Singular
    @JsonProperty("available_actions")
    private final List<String> availableActions;

    @Valid
    @Singular
    @JsonProperty("capabilities")
    private final List<ApiCapability> capabilities;

    @Valid
    @Singular
    @JsonProperty("types")
    private final List<ApiTypeConfig> types;
}
