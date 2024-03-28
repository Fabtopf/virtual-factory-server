package de.cybine.factory.config;

import de.cybine.factory.api.v1.sensor.SensorFeed;
import de.cybine.quarkus.util.event.EventManager;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.Dependent;
import lombok.RequiredArgsConstructor;

@Startup
@Dependent
@RequiredArgsConstructor
public class EventManagerConfig
{
    private final EventManager eventManager;

    private final SensorFeed sensorFeed;

    @PostConstruct
    void setup( )
    {
        this.eventManager.registerHandlers(this.sensorFeed);
    }
}
