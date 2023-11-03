package de.cybine.factory.util.datasource;

import lombok.*;

import java.util.List;

@Data
@Builder(builderClassName = "Generator")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DatasourceCountInfo
{
    @Singular("key")
    private final List<?> groupKey;

    private final long count;
}
