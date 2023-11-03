package de.cybine.factory.data.util.password;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.cybine.factory.data.util.primitive.StringPrimitive;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@EqualsAndHashCode
@RequiredArgsConstructor(staticName = "of")
@Schema(type = SchemaType.STRING, implementation = String.class)
public class Password implements StringPrimitive
{
    @JsonIgnore
    @Schema(hidden = true)
    private final String value;

    public String asString( )
    {
        return this.value;
    }

    public BCryptPasswordHash toBCryptHash( )
    {
        return BCryptPasswordHash.of(this, false);
    }

    public BCryptPasswordHash toBCryptHash(boolean ignoreLengthConstraint)
    {
        return BCryptPasswordHash.of(this, ignoreLengthConstraint);
    }

    public EnhancedBCryptPasswordHash toEnhancedBCryptHash( )
    {
        return EnhancedBCryptPasswordHash.of(this);
    }

    @Override
    public String toString( )
    {
        return "******";
    }
}
