package de.cybine.factory.util.api.converter;

import de.cybine.factory.exception.ServiceException;
import de.cybine.factory.exception.api.UnknownApiContextException;
import de.cybine.factory.exception.datasource.UnknownRelationException;
import de.cybine.factory.util.api.ApiFieldResolver;
import de.cybine.factory.util.api.ApiFieldResolverContext;
import de.cybine.factory.util.api.query.*;
import de.cybine.factory.util.converter.ConversionHelper;
import de.cybine.factory.util.converter.Converter;
import de.cybine.factory.util.datasource.*;
import io.quarkus.arc.Arc;

import java.lang.reflect.Type;

public class ApiQueryConverter implements Converter<ApiQuery, DatasourceQuery>
{
    public static final String CONTEXT_PROPERTY   = "context";
    public static final String DATA_TYPE_PROPERTY = "data-type";

    @Override
    public Class<ApiQuery> getInputType( )
    {
        return ApiQuery.class;
    }

    @Override
    public Class<DatasourceQuery> getOutputType( )
    {
        return DatasourceQuery.class;
    }

    @Override
    public DatasourceQuery convert(ApiQuery input, ConversionHelper helper)
    {
        return DatasourceQuery.builder()
                              .pagination(helper.toItem(ApiPaginationInfo.class, DatasourcePaginationInfo.class)
                                                .map(input::getPagination))
                              .condition(helper.toItem(ApiConditionInfo.class, DatasourceConditionInfo.class)
                                               .map(input::getCondition))
                              .order(helper.toList(ApiOrderInfo.class, DatasourceOrderInfo.class)
                                           .apply(input::getOrder))
                              .relations(helper.toList(ApiRelationInfo.class, DatasourceRelationInfo.class)
                                               .apply(input::getRelations))
                              .build();
    }

    static DatasourceFieldPath getFieldPathOrThrow(ConversionHelper helper, String name)
    {
        if (name == null)
            throw new UnknownRelationException("No field name specified");

        String contextName = helper.getContextOrThrow(ApiQueryConverter.CONTEXT_PROPERTY);
        ServiceException unknownContextError = new UnknownApiContextException("Unable to find context").addData(
                ApiQueryConverter.CONTEXT_PROPERTY, contextName);

        ApiFieldResolver resolver = Arc.container().select(ApiFieldResolver.class).get();
        ApiFieldResolverContext context = resolver.findContext(contextName).orElseThrow(( ) -> unknownContextError);

        Class<?> type = helper.getContextOrThrow(ApiQueryConverter.DATA_TYPE_PROPERTY);
        Type dataType = resolver.findRepresentationType(type).orElse(type);

        ServiceException unknownFieldError = new UnknownApiContextException("Unable to find field").addData("name",
                name).addData("type", dataType.getTypeName()).addData(ApiQueryConverter.CONTEXT_PROPERTY, contextName);

        return context.findField(dataType, name).orElseThrow(( ) -> unknownFieldError);
    }
}