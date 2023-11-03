package de.cybine.factory.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import de.cybine.factory.service.action.ActionDataTypeRegistry;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.Dependent;
import lombok.RequiredArgsConstructor;

@Startup
@Dependent
@RequiredArgsConstructor
public class ActionDataTypeConfig
{
    private final ActionDataTypeRegistry registry;

    private final ObjectMapper objectMapper;

    @PostConstruct
    public void setup( )
    {
        TypeFactory typeFactory = this.objectMapper.getTypeFactory();
    }
}