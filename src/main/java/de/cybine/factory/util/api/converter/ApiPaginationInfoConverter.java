package de.cybine.factory.util.api.converter;

import de.cybine.factory.util.api.query.ApiPaginationInfo;
import de.cybine.factory.util.converter.ConversionHelper;
import de.cybine.factory.util.converter.Converter;
import de.cybine.factory.util.datasource.DatasourcePaginationInfo;

public class ApiPaginationInfoConverter implements Converter<ApiPaginationInfo, DatasourcePaginationInfo>
{
    @Override
    public Class<ApiPaginationInfo> getInputType( )
    {
        return ApiPaginationInfo.class;
    }

    @Override
    public Class<DatasourcePaginationInfo> getOutputType( )
    {
        return DatasourcePaginationInfo.class;
    }

    @Override
    public DatasourcePaginationInfo convert(ApiPaginationInfo input, ConversionHelper helper)
    {
        return DatasourcePaginationInfo.builder()
                                       .size(input.getSize().orElse(null))
                                       .offset(input.getOffset().orElse(null))
                                       .includeTotal(input.includeTotal())
                                       .build();
    }
}
