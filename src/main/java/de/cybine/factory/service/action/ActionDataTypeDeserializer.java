package de.cybine.factory.service.action;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import io.quarkus.arc.Arc;

public class ActionDataTypeDeserializer implements Converter<String, JavaType>
{
    @Override
    public JavaType getInputType(TypeFactory typeFactory)
    {
        return typeFactory.constructType(String.class);
    }

    @Override
    public JavaType getOutputType(TypeFactory typeFactory)
    {
        return typeFactory.constructType(JavaType.class);
    }

    @Override
    public JavaType convert(String value)
    {
        ActionDataTypeRegistry registry = Arc.container().select(ActionDataTypeRegistry.class).get();
        return registry.findType(value).orElseThrow();
    }
}

