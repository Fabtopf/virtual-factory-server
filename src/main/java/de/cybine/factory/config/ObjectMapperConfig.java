package de.cybine.factory.config;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import io.quarkus.jackson.ObjectMapperCustomizer;
import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.Dependent;

@Startup
@Dependent
public class ObjectMapperConfig implements ObjectMapperCustomizer
{
    @Override
    public void customize(ObjectMapper mapper)
    {
        mapper.registerModule(new Jdk8Module());
        mapper.setSerializationInclusion(Include.NON_ABSENT);
    }
}
