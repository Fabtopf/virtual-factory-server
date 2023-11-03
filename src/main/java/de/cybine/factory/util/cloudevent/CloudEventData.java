package de.cybine.factory.util.cloudevent;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.type.TypeFactory;
import de.cybine.factory.exception.converter.EntityConversionException;
import de.cybine.factory.service.action.ActionDataTypeDeserializer;
import de.cybine.factory.service.action.ActionDataTypeSerializer;
import io.quarkus.arc.Arc;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Data
@Jacksonized
@AllArgsConstructor
@Builder(builderClassName = "Generator")
public class CloudEventData<T>
{
    @JsonProperty("@type")
    @JsonSerialize(converter = ActionDataTypeSerializer.class)
    @JsonDeserialize(converter = ActionDataTypeDeserializer.class)
    private final JavaType type;

    @JsonProperty("value")
    private final T value;

    public String toBase64( )
    {
        try
        {
            ObjectMapper mapper = Arc.container().select(ObjectMapper.class).get();
            return new String(Base64.getEncoder().encode(mapper.writeValueAsBytes(this)), StandardCharsets.UTF_8);
        }
        catch (JsonProcessingException exception)
        {
            throw new EntityConversionException("Could not serialize to base64", exception);
        }
    }

    public static <T> CloudEventData<T> of(T value)
    {
        TypeFactory typeFactory = Arc.container().select(ObjectMapper.class).get().getTypeFactory();
        return new CloudEventData<>(typeFactory.constructType(value.getClass()), value);
    }

    public static <T> CloudEventData<T> of(T value, Class<?> containerType, Class<?>... parameterTypes)
    {
        TypeFactory typeFactory = Arc.container().select(ObjectMapper.class).get().getTypeFactory();
        return new CloudEventData<>(typeFactory.constructParametricType(containerType, parameterTypes), value);
    }

    @SuppressWarnings("unchecked")
    public static <T> CloudEventData<T> fromBase64(String data)
    {
        try
        {
            ObjectMapper mapper = Arc.container().select(ObjectMapper.class).get();
            return mapper.readValue(Base64.getDecoder().decode(data), CloudEventData.class);
        }
        catch (IOException exception)
        {
            throw new EntityConversionException("Could not deserialize base64", exception);
        }
    }
}
