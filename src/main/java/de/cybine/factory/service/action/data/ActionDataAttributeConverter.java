package de.cybine.factory.service.action.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.arc.Arc;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Converter
public class ActionDataAttributeConverter implements AttributeConverter<ActionData<?>, String>
{
    @Override
    public String convertToDatabaseColumn(ActionData<?> attribute)
    {
        if (attribute == null)
            return null;

        return attribute.toJson();
    }

    @Override
    public ActionData<?> convertToEntityAttribute(String dbData)
    {
        if(dbData == null)
            return null;

        return ActionData.fromJson(dbData);
    }

    private ObjectMapper getObjectMapper( )
    {
        return Arc.container().select(ObjectMapper.class).get();
    }
}