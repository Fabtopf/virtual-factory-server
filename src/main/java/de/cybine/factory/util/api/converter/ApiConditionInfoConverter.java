package de.cybine.factory.util.api.converter;

import de.cybine.factory.util.api.query.ApiConditionDetail;
import de.cybine.factory.util.api.query.ApiConditionInfo;
import de.cybine.factory.util.converter.ConversionHelper;
import de.cybine.factory.util.converter.Converter;
import de.cybine.factory.util.datasource.DatasourceConditionDetail;
import de.cybine.factory.util.datasource.DatasourceConditionInfo;

import java.util.Collection;

public class ApiConditionInfoConverter implements Converter<ApiConditionInfo, DatasourceConditionInfo>
{
    @Override
    public Class<ApiConditionInfo> getInputType( )
    {
        return ApiConditionInfo.class;
    }

    @Override
    public Class<DatasourceConditionInfo> getOutputType( )
    {
        return DatasourceConditionInfo.class;
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public DatasourceConditionInfo convert(ApiConditionInfo input, ConversionHelper helper)
    {
        return DatasourceConditionInfo.builder()
                                      .type(input.getType())
                                      .isInverted(input.isInverted())
                                      .details((Collection) helper.toList(ApiConditionDetail.class,
                                              DatasourceConditionDetail.class).apply(input::getDetails))
                                      .subConditions(
                                              helper.toList(ApiConditionInfo.class, DatasourceConditionInfo.class)
                                                    .apply(input::getSubConditions))
                                      .build();
    }
}
