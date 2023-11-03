package de.cybine.factory.data.util.password;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.Pattern;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.mindrot.jbcrypt.BCrypt;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(staticName = "of")
@Schema(type = SchemaType.STRING, implementation = String.class, pattern = PasswordHash.BCRYPT_REGEX)
public class BCryptPasswordHash implements PasswordHash
{
    @JsonValue
    @Schema(hidden = true)
    @Pattern(regexp = PasswordHash.BCRYPT_REGEX)
    private final String value;

    @Override
    public String asString( )
    {
        return this.value;
    }

    @Override
    public boolean isSame(Password password)
    {
        return BCrypt.checkpw(password.asString(), this.value);
    }

    public static BCryptPasswordHash of(Password password)
    {
        return BCryptPasswordHash.of(password, false);
    }

    public static BCryptPasswordHash of(Password password, boolean ignoreLengthConstraint)
    {
        if (!ignoreLengthConstraint && password.asString().getBytes().length > 72)
            throw new IllegalArgumentException("Password too long: BCrypt only supports 72 bytes before truncating.");

        return BCryptPasswordHash.of(BCrypt.hashpw(password.asString(), BCrypt.gensalt()));
    }
}
