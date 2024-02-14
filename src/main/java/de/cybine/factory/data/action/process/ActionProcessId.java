package de.cybine.factory.data.action.process;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import de.cybine.quarkus.data.util.UUIDv7;
import de.cybine.quarkus.data.util.primitive.Id;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Data
@RequiredArgsConstructor(staticName = "of")
@Schema(type = SchemaType.STRING, implementation = UUID.class)
public class ActionProcessId implements Serializable, Id<UUID>
{
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonValue
    @Schema(hidden = true)
    private final UUID value;

    public static ActionProcessId create( )
    {
        return ActionProcessId.of(UUIDv7.generate());
    }

    public static class Deserializer extends JsonDeserializer<ActionProcessId>
    {
        @Override
        public ActionProcessId deserialize(JsonParser p, DeserializationContext ctxt) throws IOException
        {
            String value = p.nextTextValue();
            if(value == null)
                return null;

            return ActionProcessId.of(UUID.fromString(value));
        }
    }
}
