package de.cybine.factory.util.api.converter;

import de.cybine.factory.util.api.query.ApiOrderInfo;
import de.cybine.factory.util.converter.ConversionHelper;
import de.cybine.factory.util.converter.Converter;
import de.cybine.factory.util.datasource.DatasourceFieldPath;
import de.cybine.factory.util.datasource.DatasourceOrderInfo;

public class ApiOrderInfoConverter implements Converter<ApiOrderInfo, DatasourceOrderInfo>
{
    @Override
    public Class<ApiOrderInfo> getInputType( )
    {
        return ApiOrderInfo.class;
    }

    @Override
    public Class<DatasourceOrderInfo> getOutputType( )
    {
        return DatasourceOrderInfo.class;
    }

    @Override
    public DatasourceOrderInfo convert(ApiOrderInfo input, ConversionHelper helper)
    {
        DatasourceFieldPath path = ApiQueryConverter.getFieldPathOrThrow(helper, input.getProperty());
        return DatasourceOrderInfo.builder()
                                  .property(path.asString())
                                  .priority(input.getPriority())
                                  .isAscending(input.isAscending())
                                  .build();
    }
}
