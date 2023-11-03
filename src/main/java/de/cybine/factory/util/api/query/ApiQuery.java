package de.cybine.factory.util.api.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.Optional;

@Data
@Jacksonized
@Builder(builderClassName = "Generator")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiQuery
{
    @Valid
    @JsonProperty("pagination")
    private final ApiPaginationInfo pagination;

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

    public Optional<ApiPaginationInfo> getPagination( )
    {
        return Optional.ofNullable(this.pagination);
    }

    public Optional<ApiConditionInfo> getCondition( )
    {
        return Optional.ofNullable(this.condition);
    }
}
