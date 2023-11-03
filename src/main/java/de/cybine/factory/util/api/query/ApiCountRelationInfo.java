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
public class ApiCountRelationInfo
{
    @NotNull
    @JsonProperty("property")
    private final String property;

    @Valid
    @JsonProperty("condition")
    private final ApiConditionInfo condition;

    @Singular("groupBy")
    @JsonProperty("group_by")
    private final List<String> groupingProperties;

    public Optional<ApiConditionInfo> getCondition( )
    {
        return Optional.ofNullable(this.condition);
    }
}
