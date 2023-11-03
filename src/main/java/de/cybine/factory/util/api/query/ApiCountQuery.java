package de.cybine.factory.util.api.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Jacksonized
@Builder(builderClassName = "Generator")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiCountQuery
{
    @Valid
    @JsonProperty("condition")
    private final ApiConditionInfo condition;

    @Singular("groupBy")
    @JsonProperty("group_by")
    private final List<String> groupingProperties;

    @Valid
    @Singular
    @JsonProperty("relations")
    private final List<ApiCountRelationInfo> relations;
}
