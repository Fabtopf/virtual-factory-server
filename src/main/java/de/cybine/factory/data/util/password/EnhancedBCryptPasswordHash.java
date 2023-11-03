package de.cybine.factory.data.util.password;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.Pattern;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.mindrot.jbcrypt.BCrypt;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(staticName = "of")
@Schema(type = SchemaType.STRING, implementation = String.class, pattern = PasswordHash.BCRYPT_REGEX)
public class EnhancedBCryptPasswordHash implements PasswordHash
{
    @JsonValue
    @Schema(hidden = true)
    @Pattern(regexp = PasswordHash.BCRYPT_REGEX)
    private final String value;

    public String getSalt( )
    {
        return EnhancedBCryptPasswordHash.getSalt(this.value);
    }

    @Override
    public String asString( )
    {
        return this.value;
    }

    @Override
    public boolean isSame(Password password)
    {
        String hash = EnhancedBCryptPasswordHash.hash(password.asString(), this.getSalt());
        return BCrypt.checkpw(hash, this.value);
    }

    public static EnhancedBCryptPasswordHash of(Password password)
    {
        String salt = BCrypt.gensalt();
        String hash = EnhancedBCryptPasswordHash.hash(password.asString(), EnhancedBCryptPasswordHash.getSalt(salt));

        return EnhancedBCryptPasswordHash.of(BCrypt.hashpw(hash, salt));
    }

    private static String hash(String data, String salt)
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes(StandardCharsets.UTF_8));

            byte[] bytes = md.digest(data.getBytes(StandardCharsets.UTF_8));

            return HexFormat.of().formatHex(bytes);
        }
        catch (NoSuchAlgorithmException exception)
        {
            throw new IllegalArgumentException(exception);
        }
    }

    private static String getSalt(String hash)
    {
        return hash.split("\\$")[ 3 ].substring(0, 22);
    }
}
