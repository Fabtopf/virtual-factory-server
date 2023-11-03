package de.cybine.factory.service.action;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.cybine.factory.exception.action.ActionProcessingException;
import io.quarkus.arc.Arc;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.HashMap;
import java.util.Map;

@Converter
public class ActionDataAttributeConverter implements AttributeConverter<ActionData<?>, String>
{
    @Override
    public String convertToDatabaseColumn(ActionData<?> attribute)
    {
        if (attribute == null)
            return null;

        try
        {
            String type = this.getRegistry()
                              .findTypeName(attribute.type())
                              .orElseThrow(( ) -> new ActionProcessingException("Unknown action data type"));

            Map<String, Object> data = new HashMap<>();
            data.put("@type", type);
            data.put("data", attribute.data());

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
            JavaType type = this.getRegistry()
                                .findType(jsonNode.findValue("@type").asText())
                                .orElseThrow(( ) -> new ActionProcessingException("Unknown action data type"));

            Object data = this.getObjectMapper().treeToValue(jsonNode.findValue("data"), type);

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

    private ActionDataTypeRegistry getRegistry( )
    {
        return Arc.container().select(ActionDataTypeRegistry.class).get();
    }
}