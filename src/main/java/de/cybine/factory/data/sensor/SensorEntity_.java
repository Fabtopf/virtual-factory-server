package de.cybine.factory.data.sensor;

import de.cybine.quarkus.util.datasource.DatasourceField;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import lombok.experimental.UtilityClass;

@UtilityClass
@SuppressWarnings("unused")
@StaticMetamodel(SensorEntity.class)
public class SensorEntity_
{
    public static final String TABLE  = "sensor";
    public static final String ENTITY = "Sensor";

    public static final String ID_COLUMN           = "id";
    public static final String REFERENCE_ID_COLUMN = "reference_id";
    public static final String TYPE_COLUMN         = "type";
    public static final String DESCRIPTION_COLUMN  = "description";

    // @formatter:off
    public static final DatasourceField ID           =
            DatasourceField.property(SensorEntity.class, "id", Long.class);
    public static final DatasourceField REFERENCE_ID =
            DatasourceField.property(SensorEntity.class, "referenceId", String.class);
    public static final DatasourceField TYPE         =
            DatasourceField.property(SensorEntity.class, "type", String.class);
    public static final DatasourceField DESCRIPTION  =
            DatasourceField.property(SensorEntity.class, "description", String.class);
    // @formatter:on

    public static volatile SingularAttribute<SensorEntity, Long>   id;
    public static volatile SingularAttribute<SensorEntity, String> referenceId;
    public static volatile SingularAttribute<SensorEntity, String> type;
    public static volatile SingularAttribute<SensorEntity, String> description;
}
