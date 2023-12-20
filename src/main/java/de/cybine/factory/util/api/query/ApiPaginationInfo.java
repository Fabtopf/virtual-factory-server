package de.cybine.factory.util.api.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.enterprise.context.RequestScoped;
import jakarta.validation.constraints.Null;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Optional;

@Data
@RequestScoped
public class ApiPaginationInfo
{
    @JsonProperty("size")
    private Integer size;

    @JsonProperty("offset")
    private Integer offset;

    @Accessors(fluent = true)
    @JsonProperty("include_total")
    private boolean includeTotal = false;

    @JsonProperty("total")
    @Null(message = "total is calculated from datasource")
    private Long total;

    public Optional<Integer> getSize( )
    {
        return Optional.ofNullable(this.size);
    }

    @JsonIgnore
    public Optional<Long> getSizeAsLong( )
    {
        return this.getSize().map(Long::valueOf);
    }

    public Optional<Integer> getOffset( )
    {
        return Optional.ofNullable(this.offset);
    }

    @JsonIgnore
    public Optional<Long> getOffsetAsLong( )
    {
        return this.getOffset().map(Long::valueOf);
    }

    public Optional<Long> getTotal( )
    {
        return Optional.ofNullable(this.total);
    }
}
