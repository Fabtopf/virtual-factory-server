package de.cybine.factory.util.api.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.Optional;

@Data
@Jacksonized
@Builder(builderClassName = "Generator")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiRelationInfo
{
    @NotNull
    @JsonProperty("property")
    private final String property;

    @Builder.Default
    @JsonProperty("fetch")
    private final boolean fetch = false;

    @Valid
    @JsonProperty("condition")
    private final ApiConditionInfo condition;

    @Valid
    @Singular("order")
    @JsonProperty("order")
    private final List<ApiOrderInfo> order;

    @Valid
    @Singular
    @JsonProperty("relations")
    private final List<ApiRelationInfo> relations;

    public Optional<ApiConditionInfo> getCondition( )
    {
        return Optional.ofNullable(this.condition);
    }
}
