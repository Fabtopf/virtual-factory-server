package de.cybine.factory.data.action.metadata;

import com.fasterxml.jackson.annotation.JsonValue;
import de.cybine.factory.data.util.primitive.Id;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;

@Data
@RequiredArgsConstructor(staticName = "of")
@Schema(type = SchemaType.NUMBER, implementation = Long.class)
public class ActionMetadataId implements Serializable, Id<Long>
{
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonValue
    @Schema(hidden = true)
    private final Long value;
}
