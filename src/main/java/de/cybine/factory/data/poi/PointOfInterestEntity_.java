package de.cybine.factory.data.poi;

import de.cybine.quarkus.util.datasource.DatasourceField;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import lombok.experimental.UtilityClass;

@UtilityClass
@SuppressWarnings("unused")
@StaticMetamodel(PointOfInterestEntity.class)
public class PointOfInterestEntity_
{
    public static final String TABLE  = "poi";
    public static final String ENTITY = "POI";

    public static final String ID_COLUMN        = "id";
    public static final String SERVER_ID_COLUMN = "server_id";
    public static final String WORLD_COLUMN     = "world";
    public static final String X_COLUMN         = "location_x";
    public static final String Y_COLUMN         = "location_y";
    public static final String Z_COLUMN         = "location_z";
    public static final String TYPE_COLUMN      = "type";

    // @formatter:off
    public static final DatasourceField ID        =
            DatasourceField.property(PointOfInterestEntity.class, "id", Long.class);
    public static final DatasourceField SERVER_ID =
            DatasourceField.property(PointOfInterestEntity.class, "serverId", String.class);
    public static final DatasourceField WORLD     =
            DatasourceField.property(PointOfInterestEntity.class, "world", String.class);
    public static final DatasourceField X         =
            DatasourceField.property(PointOfInterestEntity.class, "xLocation", Double.class);
    public static final DatasourceField Y         =
            DatasourceField.property(PointOfInterestEntity.class, "yLocation", Double.class);
    public static final DatasourceField Z         =
            DatasourceField.property(PointOfInterestEntity.class, "zLocation", Double.class);
    public static final DatasourceField TYPE      =
            DatasourceField.property(PointOfInterestEntity.class, "type", String.class);
    // @formatter:on

    public static volatile SingularAttribute<PointOfInterestEntity, Long>   id;
    public static volatile SingularAttribute<PointOfInterestEntity, String> serverId;
    public static volatile SingularAttribute<PointOfInterestEntity, String> world;
    public static volatile SingularAttribute<PointOfInterestEntity, Double> xLocation;
    public static volatile SingularAttribute<PointOfInterestEntity, Double> yLocation;
    public static volatile SingularAttribute<PointOfInterestEntity, Double> zLocation;
    public static volatile SingularAttribute<PointOfInterestEntity, String> type;
}
