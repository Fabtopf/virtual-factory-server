package de.cybine.factory.data.poi;

import de.cybine.factory.data.util.PointLocation;
import de.cybine.factory.data.util.primitive.Id;
import de.cybine.factory.util.converter.ConversionHelper;
import de.cybine.factory.util.converter.EntityMapper;

public class PointOfInterestMapper implements EntityMapper<PointOfInterestEntity, PointOfInterest>
{
    @Override
    public Class<PointOfInterestEntity> getEntityType( )
    {
        return PointOfInterestEntity.class;
    }

    @Override
    public Class<PointOfInterest> getDataType( )
    {
        return PointOfInterest.class;
    }

    @Override
    public PointOfInterestEntity toEntity(PointOfInterest data, ConversionHelper helper)
    {
        return PointOfInterestEntity.builder()
                                    .id(data.findId().map(Id::getValue).orElse(null))
                                    .xLocation(helper.optional(data::getLocation).map(PointLocation::x).orElse(null))
                                    .yLocation(helper.optional(data::getLocation).map(PointLocation::y).orElse(null))
                                    .zLocation(helper.optional(data::getLocation).map(PointLocation::z).orElse(null))
                                    .type(data.getType())
                                    .build();
    }

    @Override
    public PointOfInterest toData(PointOfInterestEntity entity, ConversionHelper helper)
    {
        PointLocation location = null;
        if (entity.getXLocation() != null && entity.getYLocation() != null && entity.getZLocation() != null)
            location = new PointLocation(entity.getXLocation(), entity.getYLocation(), entity.getZLocation());

        return PointOfInterest.builder()
                              .id(entity.findId().map(PointOfInterestId::of).orElse(null))
                              .location(location)
                              .type(entity.getType())
                              .build();
    }
}
