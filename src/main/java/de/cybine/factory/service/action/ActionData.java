package de.cybine.factory.service.action;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.quarkus.arc.Arc;

public record ActionData<T>(
        @JsonProperty("@type") @JsonSerialize(converter = ActionDataTypeSerializer.class) @JsonDeserialize(converter
                = ActionDataTypeDeserializer.class) JavaType type,
        @JsonProperty("value") T value)
{
    public static <T> ActionData<T> of(T data)
    {
        TypeFactory typeFactory = Arc.container().select(ObjectMapper.class).get().getTypeFactory();
        return new ActionData<>(typeFactory.constructType(data.getClass()), data);
    }

    public static <T> ActionData<T> of(T data, Class<?> containerType, Class<?>... parameterTypes)
    {
        TypeFactory typeFactory = Arc.container().select(ObjectMapper.class).get().getTypeFactory();
        return new ActionData<>(typeFactory.constructParametricType(containerType, parameterTypes), data);
    }
}
