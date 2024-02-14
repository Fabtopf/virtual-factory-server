package de.cybine.factory.config;

import de.cybine.factory.data.action.context.ActionContextMapper;
import de.cybine.factory.data.action.process.ActionProcessMapper;
import de.cybine.factory.data.poi.PointOfInterestMapper;
import de.cybine.factory.data.sensor.SensorMapper;
import de.cybine.quarkus.util.converter.ConverterRegistry;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.Dependent;
import lombok.RequiredArgsConstructor;

@Startup
@Dependent
@RequiredArgsConstructor
public class ConverterConfig
{
    private final ConverterRegistry registry;

    @PostConstruct
    public void setup( )
    {
        this.registry.addEntityMapper(new ActionContextMapper());
        this.registry.addEntityMapper(new ActionProcessMapper());

        this.registry.addEntityMapper(new PointOfInterestMapper());
        this.registry.addEntityMapper(new SensorMapper());
    }
}
