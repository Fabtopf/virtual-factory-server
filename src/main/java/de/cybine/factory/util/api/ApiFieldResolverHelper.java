package de.cybine.factory.util.api;

import de.cybine.factory.util.datasource.DatasourceField;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Type;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class ApiFieldResolverHelper
{
    private final ApiFieldResolver resolver;

    private final Type dataType;

    public ApiFieldResolverHelper registerField(String alias, DatasourceField field)
    {
        this.resolver.registerField(this.dataType, alias, field);
        return this;
    }
}