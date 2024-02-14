package de.cybine.factory.data.sensor;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.cybine.quarkus.util.WithId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

@Data
@Jacksonized
@Builder(builderClassName = "Generator")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Sensor implements Serializable, WithId<SensorId>
{
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private final SensorId id;

    @JsonProperty("reference_id")
    private final String referenceId;

    @JsonProperty("type")
    private final String type;

    @JsonProperty("description")
    private final String description;

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
