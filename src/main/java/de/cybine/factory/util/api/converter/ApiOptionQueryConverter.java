package de.cybine.factory.util.api.converter;

import de.cybine.factory.exception.datasource.UnknownRelationException;
import de.cybine.factory.util.api.query.ApiConditionInfo;
import de.cybine.factory.util.api.query.ApiOptionQuery;
import de.cybine.factory.util.api.query.ApiPaginationInfo;
import de.cybine.factory.util.converter.ConversionHelper;
import de.cybine.factory.util.converter.Converter;
import de.cybine.factory.util.datasource.DatasourceConditionInfo;
import de.cybine.factory.util.datasource.DatasourceFieldPath;
import de.cybine.factory.util.datasource.DatasourcePaginationInfo;
import de.cybine.factory.util.datasource.DatasourceQuery;

public class ApiOptionQueryConverter implements Converter<ApiOptionQuery, DatasourceQuery>
{
    @Override
    public Class<ApiOptionQuery> getInputType( )
    {
        return ApiOptionQuery.class;
    }

    @Override
    public Class<DatasourceQuery> getOutputType( )
    {
        return DatasourceQuery.class;
    }

    @Override
    public DatasourceQuery convert(ApiOptionQuery input, ConversionHelper helper)
    {
        DatasourceFieldPath path = ApiQueryConverter.getFieldPathOrThrow(helper, input.getProperty());
        if (path.getLength() > 1)
            throw new UnknownRelationException(
                    String.format("Cannot traverse multiple elements while fetching options for field (%s)",
                            input.getProperty()));

        if (path.getLast().isRelation())
            throw new UnknownRelationException(
                    String.format("Cannot fetch options for relation (%s)", input.getProperty()));

        return DatasourceQuery.builder()
                              .property(path.asString())
                              .pagination(helper.toItem(ApiPaginationInfo.class, DatasourcePaginationInfo.class)
                                                .map(input::getPagination))
                              .condition(helper.toItem(ApiConditionInfo.class, DatasourceConditionInfo.class)
                                               .map(input::getCondition))
                              .build();
    }
}
