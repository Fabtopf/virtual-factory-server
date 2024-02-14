package de.cybine.factory.data.sensor;

import de.cybine.quarkus.util.WithId;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

@Data
@NoArgsConstructor
@Table(name = SensorEntity_.TABLE)
@Entity(name = SensorEntity_.ENTITY)
@Builder(builderClassName = "Generator")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SensorEntity implements Serializable, WithId<Long>
{
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = SensorEntity_.ID_COLUMN, nullable = false, unique = true)
    private Long id;

    @Column(name = SensorEntity_.REFERENCE_ID_COLUMN, nullable = false, unique = true)
    private String referenceId;

    @Column(name = SensorEntity_.TYPE_COLUMN, nullable = false)
    private String type;

    @Column(name = SensorEntity_.DESCRIPTION_COLUMN)
    private String description;

    public Optional<String> getDescription( )
    {
        return Optional.ofNullable(this.description);
    }

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
