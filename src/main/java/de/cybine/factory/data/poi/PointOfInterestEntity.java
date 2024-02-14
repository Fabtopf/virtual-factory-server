package de.cybine.factory.data.poi;

import de.cybine.quarkus.util.WithId;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@Builder(builderClassName = "Generator")
@Table(name = PointOfInterestEntity_.TABLE)
@Entity(name = PointOfInterestEntity_.ENTITY)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PointOfInterestEntity implements Serializable, WithId<Long>
{
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = PointOfInterestEntity_.ID_COLUMN, nullable = false, unique = true)
    private Long id;

    @Column(name = PointOfInterestEntity_.X_COLUMN, nullable = false)
    private Double xLocation;

    @Column(name = PointOfInterestEntity_.Y_COLUMN, nullable = false)
    private Double yLocation;

    @Column(name = PointOfInterestEntity_.Z_COLUMN, nullable = false)
    private Double zLocation;

    @Column(name = PointOfInterestEntity_.TYPE_COLUMN, nullable = false)
    private String type;

    @Override
    public boolean equals(Object other)
    {
        if(other == null)
            return false;

        if(this.getClass() != other.getClass())
            return false;

        WithId<?> that = ((WithId<?>) other);
        if (this.findId().isEmpty() || that.findId().isEmpty())
            return false;

        return Objects.equals(this.getId(), that.getId());
    }

    @Override
    public int hashCode( )
    {
        return this.findId().map(Object::hashCode).orElse(0);
    }
}
