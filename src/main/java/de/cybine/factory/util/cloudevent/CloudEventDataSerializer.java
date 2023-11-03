package de.cybine.factory.util.cloudevent;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

public class CloudEventDataSerializer implements Converter<CloudEventData<?>, String>
{
    @Override
    public JavaType getInputType(TypeFactory typeFactory)
    {
        return typeFactory.constructParametricType(CloudEventData.class, Object.class);
    }

    @Override
    public JavaType getOutputType(TypeFactory typeFactory)
    {
        return typeFactory.constructType(String.class);
    }

    @Override
    public String convert(CloudEventData<?> value)
    {
        return value.toBase64();
    }
}
