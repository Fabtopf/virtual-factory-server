package de.cybine.factory.service.action;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.cybine.factory.exception.action.ActionProcessingException;
import io.quarkus.arc.Arc;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Converter
public class ActionDataAttributeConverter implements AttributeConverter<ActionData<?>, String>
{
    private final ActionDataTypeSerializer   typeSerializer   = new ActionDataTypeSerializer();
    private final ActionDataTypeDeserializer typeDeserializer = new ActionDataTypeDeserializer();

    @Override
    public String convertToDatabaseColumn(ActionData<?> attribute)
    {
        if (attribute == null)
            return null;

        try
        {
            Map<String, Object> data = new HashMap<>();
            data.put("@type", this.typeSerializer.convert(attribute.type()));
            data.put("value", attribute.value());

            return this.getObjectMapper().writeValueAsString(data);
        }
        catch (JsonProcessingException exception)
        {
            throw new ActionProcessingException(exception);
        }
    }

    @Override
    public ActionData<?> convertToEntityAttribute(String dbData)
    {
        if (dbData == null)
            return null;

        try
        {
            JsonNode jsonNode = this.getObjectMapper().readTree(dbData);
            String typeName = jsonNode.findValue("@type").asText();

            JavaType type = this.typeDeserializer.convert(typeName);
            Object data = this.getObjectMapper().treeToValue(jsonNode.findValue("value"), type);

            return new ActionData<>(type, data);
        }
        catch (JsonProcessingException exception)
        {
            throw new ActionProcessingException(exception);
        }
    }

    private ObjectMapper getObjectMapper( )
    {
        return Arc.container().select(ObjectMapper.class).get();
    }
}