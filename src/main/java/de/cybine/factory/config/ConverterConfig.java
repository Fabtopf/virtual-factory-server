package de.cybine.factory.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.cybine.factory.data.action.context.ActionContextMapper;
import de.cybine.factory.data.action.metadata.ActionMetadataMapper;
import de.cybine.factory.data.action.process.ActionProcessMapper;
import de.cybine.factory.data.poi.PointOfInterestMapper;
import de.cybine.factory.data.sensor.SensorMapper;
import de.cybine.factory.util.api.converter.*;
import de.cybine.factory.util.cloudevent.CloudEventConverter;
import de.cybine.factory.util.converter.ConverterRegistry;
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

    private final ObjectMapper      objectMapper;
    private final ApplicationConfig applicationConfig;

    @PostConstruct
    public void setup( )
    {
        this.registry.addConverter(new ApiConditionDetailConverter(this.objectMapper));
        this.registry.addConverter(new ApiConditionInfoConverter());
        this.registry.addConverter(new ApiCountQueryConverter());
        this.registry.addConverter(new ApiCountRelationConverter());
        this.registry.addConverter(new ApiQueryConverter());
        this.registry.addConverter(new ApiOptionQueryConverter());
        this.registry.addConverter(new ApiOrderInfoConverter());
        this.registry.addConverter(new ApiOptionQueryConverter());
        this.registry.addConverter(new ApiPaginationInfoConverter());
        this.registry.addConverter(new ApiRelationInfoConverter(this.applicationConfig));
        this.registry.addEntityMapper(new CountInfoMapper());

        this.registry.addEntityMapper(new ActionContextMapper());
        this.registry.addEntityMapper(new ActionMetadataMapper());
        this.registry.addEntityMapper(new ActionProcessMapper());
        this.registry.addConverter(new CloudEventConverter(this.applicationConfig));

        this.registry.addEntityMapper(new PointOfInterestMapper());
        this.registry.addEntityMapper(new SensorMapper());
    }
}
