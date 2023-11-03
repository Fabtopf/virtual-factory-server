package de.cybine.factory.util.api;

import de.cybine.factory.util.BiTuple;
import de.cybine.factory.util.datasource.DatasourceField;
import de.cybine.factory.util.datasource.DatasourceFieldPath;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.util.Optional;
import java.util.function.Function;

// TODO: Add permission processing
@Data
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class ApiFieldResolverContext
{
    private final String contextName;

    @Getter(AccessLevel.NONE)
    private final Function<BiTuple<Type, String>, DatasourceField> fieldSupplier;

    public Optional<DatasourceFieldPath> findField(Type responseType, String fieldName)
    {
        if (fieldName == null || fieldName.isBlank())
            return Optional.empty();

        DatasourceField field = null;
        String[] names = fieldName.split("\\.");
        DatasourceFieldPath.Generator path = DatasourceFieldPath.builder();
        for (String name : names)
        {
            BiTuple<Type, String> fieldDefinition = new BiTuple<>(responseType, name);
            if (field != null)
                fieldDefinition = new BiTuple<>(field.getFieldType(), name);

            field = this.getField(fieldDefinition);
            if (field == null)
                return Optional.empty();

            path.field(field);
        }

        return Optional.of(path.build());
    }

    private DatasourceField getField(BiTuple<Type, String> fieldDefinition)
    {
        return this.fieldSupplier.apply(fieldDefinition);
    }
}
