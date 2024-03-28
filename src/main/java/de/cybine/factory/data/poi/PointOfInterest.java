package de.cybine.factory.data.poi;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.cybine.factory.data.util.PointLocation;
import de.cybine.quarkus.util.WithId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Data
@Jacksonized
@Builder(builderClassName = "Generator")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PointOfInterest implements Serializable, WithId<PointOfInterestId>
{
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private final PointOfInterestId id;

    @JsonProperty("server_id")
    private final String serverId;

    @JsonProperty("world")
    private final String world;

    @JsonProperty("location")
    private final PointLocation location;

    @JsonProperty("type")
    private final String type;

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
