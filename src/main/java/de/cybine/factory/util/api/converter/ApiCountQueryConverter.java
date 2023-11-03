package de.cybine.factory.util.api.converter;

import de.cybine.factory.util.api.query.ApiConditionInfo;
import de.cybine.factory.util.api.query.ApiCountQuery;
import de.cybine.factory.util.api.query.ApiCountRelationInfo;
import de.cybine.factory.util.converter.ConversionHelper;
import de.cybine.factory.util.converter.Converter;
import de.cybine.factory.util.datasource.DatasourceConditionInfo;
import de.cybine.factory.util.datasource.DatasourceFieldPath;
import de.cybine.factory.util.datasource.DatasourceQuery;
import de.cybine.factory.util.datasource.DatasourceRelationInfo;

public class ApiCountQueryConverter implements Converter<ApiCountQuery, DatasourceQuery>
{
    @Override
    public Class<ApiCountQuery> getInputType( )
    {
        return ApiCountQuery.class;
    }

    @Override
    public Class<DatasourceQuery> getOutputType( )
    {
        return DatasourceQuery.class;
    }

    @Override
    public DatasourceQuery convert(ApiCountQuery input, ConversionHelper helper)
    {
        return DatasourceQuery.builder()
                              .groupingProperties(input.getGroupingProperties()
                                                       .stream()
                                                       .map(item -> ApiQueryConverter.getFieldPathOrThrow(helper, item))
                                                       .map(DatasourceFieldPath::asString)
                                                       .toList())
                              .condition(helper.toItem(ApiConditionInfo.class, DatasourceConditionInfo.class)
                                               .apply(input::getCondition))
                              .relations(helper.toList(ApiCountRelationInfo.class, DatasourceRelationInfo.class)
                                               .apply(input::getRelations))
                              .build();
    }
}
