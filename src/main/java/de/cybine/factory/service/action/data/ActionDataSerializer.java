package de.cybine.factory.service.action.data;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

public class ActionDataSerializer implements Converter<ActionData<?>, String>
{
    @Override
    public JavaType getInputType(TypeFactory typeFactory)
    {
        return typeFactory.constructParametricType(ActionData.class, Object.class);
    }

    @Override
    public JavaType getOutputType(TypeFactory typeFactory)
    {
        return typeFactory.constructType(String.class);
    }

    @Override
    public String convert(ActionData<?> value)
    {
        return value.toBase64();
    }
}
