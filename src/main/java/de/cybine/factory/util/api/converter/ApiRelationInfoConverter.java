package de.cybine.factory.util.api.converter;

import de.cybine.factory.exception.datasource.UnknownRelationException;
import de.cybine.factory.util.api.query.ApiConditionInfo;
import de.cybine.factory.util.api.query.ApiOrderInfo;
import de.cybine.factory.util.api.query.ApiRelationInfo;
import de.cybine.factory.util.converter.ConversionHelper;
import de.cybine.factory.util.converter.Converter;
import de.cybine.factory.util.datasource.DatasourceConditionInfo;
import de.cybine.factory.util.datasource.DatasourceFieldPath;
import de.cybine.factory.util.datasource.DatasourceOrderInfo;
import de.cybine.factory.util.datasource.DatasourceRelationInfo;

import java.util.Collections;

public class ApiRelationInfoConverter implements Converter<ApiRelationInfo, DatasourceRelationInfo>
{
    @Override
    public Class<ApiRelationInfo> getInputType( )
    {
        return ApiRelationInfo.class;
    }

    @Override
    public Class<DatasourceRelationInfo> getOutputType( )
    {
        return DatasourceRelationInfo.class;
    }

    @Override
    public DatasourceRelationInfo convert(ApiRelationInfo input, ConversionHelper helper)
    {
        DatasourceFieldPath path = ApiQueryConverter.getFieldPathOrThrow(helper, input.getProperty());
        if (path.getLength() > 1)
            throw new UnknownRelationException(
                    String.format("Cannot traverse multiple elements while resolving relations (%s)",
                            input.getProperty()));

        helper.withContext(ApiQueryConverter.DATA_TYPE_PROPERTY, path.getLast().getFieldType());

        return DatasourceRelationInfo.builder()
                                     .property(path.asString())
                                     .fetch(input.isFetch())
                                     .condition(helper.toItem(ApiConditionInfo.class, DatasourceConditionInfo.class)
                                                      .map(input::getCondition))
                                     .order(helper.toList(ApiOrderInfo.class, DatasourceOrderInfo.class)
                                                  .apply(input::getOrder))
                                     // Only allow one relation layer via the API
                                     .relations(Collections.emptyList())
                                     .build();
    }
}
