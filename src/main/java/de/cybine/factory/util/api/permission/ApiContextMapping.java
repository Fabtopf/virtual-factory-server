package de.cybine.factory.util.api.permission;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
public class ApiContextMapping implements Comparable<ApiContextMapping>
{
    @NotNull
    @NotBlank
    @JsonProperty("name")
    private final String name;

    @NotNull
    @NotBlank
    @JsonProperty("permission")
    private final String permission;

    @Min(1)
    @Builder.Default
    @JsonProperty("priority")
    private int priority = 100;

    @Override
    public int compareTo(ApiContextMapping other)
    {
        return Integer.compare(this.getPriority(), other.getPriority());
    }
}
