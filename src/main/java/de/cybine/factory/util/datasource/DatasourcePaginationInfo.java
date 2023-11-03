package de.cybine.factory.util.datasource;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.Optional;

@Data
@Builder(builderClassName = "Generator")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DatasourcePaginationInfo
{
    private final Integer size;
    private final Integer offset;

    @Builder.Default
    @Accessors(fluent = true)
    private final boolean includeTotal = false;

    @With
    private Long total;

    public Optional<Integer> getSize( )
    {
        return Optional.ofNullable(this.size);
    }

    public Optional<Integer> getOffset( )
    {
        return Optional.ofNullable(this.offset);
    }

    public Optional<Long> getTotal( )
    {
        return Optional.ofNullable(this.total);
    }
}
