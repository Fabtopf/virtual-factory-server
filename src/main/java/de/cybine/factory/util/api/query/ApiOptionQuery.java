package de.cybine.factory.util.api.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.Optional;

@Data
@Jacksonized
@Builder(builderClassName = "Generator")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiOptionQuery
{
    @NotNull
    @JsonProperty("property")
    private final String property;

    @Valid
    @JsonProperty("pagination")
    private final ApiPaginationInfo pagination;

    @Valid
    @JsonProperty("condition")
    private final ApiConditionInfo condition;

    public Optional<ApiPaginationInfo> getPagination( )
    {
        return Optional.ofNullable(this.pagination);
    }

    public Optional<ApiConditionInfo> getCondition( )
    {
        return Optional.ofNullable(this.condition);
    }
}
