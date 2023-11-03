package de.cybine.factory.util.datasource;

import de.cybine.factory.util.BiTuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Path;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(builderClassName = "Generator")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DatasourceOrderInfo
{
    @NotNull
    private final String property;

    @Builder.Default
    private final int priority = 100;

    @Builder.Default
    private final boolean isAscending = true;

    public BiTuple<Order, Integer> toOrder(CriteriaBuilder criteriaBuilder, Path<?> parent)
    {
        Path<?> path = DatasourceFieldPath.resolvePath(parent, this.property);
        return new BiTuple<>(this.isAscending ? criteriaBuilder.asc(path) : criteriaBuilder.desc(path), this.priority);
    }
}
