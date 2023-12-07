package de.cybine.factory.data.sensor;

import de.cybine.factory.data.util.primitive.Id;
import de.cybine.factory.util.converter.ConversionHelper;
import de.cybine.factory.util.converter.EntityMapper;

public class SensorMapper implements EntityMapper<SensorEntity, Sensor>
{
    @Override
    public Class<SensorEntity> getEntityType( )
    {
        return SensorEntity.class;
    }

    @Override
    public Class<Sensor> getDataType( )
    {
        return Sensor.class;
    }

    @Override
    public SensorEntity toEntity(Sensor data, ConversionHelper helper)
    {
        return SensorEntity.builder()
                           .id(data.findId().map(Id::getValue).orElse(null))
                           .referenceId(data.getReferenceId())
                           .type(data.getType())
                           .description(data.getDescription().orElse(null))
                           .build();
    }

    @Override
    public Sensor toData(SensorEntity entity, ConversionHelper helper)
    {
        return Sensor.builder()
                     .id(entity.findId().map(SensorId::of).orElse(null))
                     .referenceId(entity.getReferenceId())
                     .type(entity.getType())
                     .description(entity.getDescription().orElse(null))
                     .build();
    }
}
