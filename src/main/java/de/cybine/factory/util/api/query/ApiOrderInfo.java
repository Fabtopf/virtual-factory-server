package de.cybine.factory.util.api.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@Builder(builderClassName = "Generator")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiOrderInfo
{
    @NotNull
    @JsonProperty("property")
    private final String property;

    @Builder.Default
    @JsonProperty("priority")
    private final int priority = 100;

    @Builder.Default
    @JsonProperty("ascending")
    private final boolean isAscending = true;
}
