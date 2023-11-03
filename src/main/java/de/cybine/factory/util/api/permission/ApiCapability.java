package de.cybine.factory.util.api.permission;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Jacksonized
@Builder(builderClassName = "Generator")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiCapability
{
    @NotNull
    @NotBlank
    @JsonProperty("name")
    private final String name;

    @JsonProperty("scopes")
    private final List<ApiCapabilityScope> scopes;
}
