package de.cybine.factory.util.datasource;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.cybine.factory.exception.TechnicalException;
import io.quarkus.arc.Arc;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

@Data
@Builder(builderClassName = "Generator")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DatasourceField
{
    private final String name;

    private final Type objectType;
    private final Type fieldType;

    private final JavaType itemType;

    private final boolean isIterable;
    private final boolean isRelation;

    public static DatasourceField property(Class<?> objectType, String name, Class<?> fieldType)
    {
        try
        {
            Field field = objectType.getDeclaredField(name);

            return DatasourceField.builder()
                                  .name(name)
                                  .objectType(objectType)
                                  .fieldType(fieldType)
                                  .itemType(DatasourceField.createType(fieldType))
                                  .isIterable(Iterable.class.isAssignableFrom(field.getType()))
                                  .isRelation(field.isAnnotationPresent(Entity.class))
                                  .build();
        }
        catch (NoSuchFieldException exception)
        {
            throw new TechnicalException("Invalid entity field definition", exception);
        }
    }

    private static JavaType createType(Type type)
    {
        return Arc.container().select(ObjectMapper.class).get().constructType(type);
    }
}
