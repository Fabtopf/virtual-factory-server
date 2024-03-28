package de.cybine.factory.service.sensor;

import de.cybine.quarkus.util.event.Event;
import lombok.Data;

@Data
public class SensorEvent implements Event
{
    public final SensorEventData data;
}
