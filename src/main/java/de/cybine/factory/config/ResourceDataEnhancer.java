package de.cybine.factory.config;

import de.cybine.factory.util.api.permission.ApiAction;
import de.cybine.factory.util.api.query.ApiPaginationInfo;
import de.cybine.factory.util.api.response.ApiResourceInfo;
import de.cybine.factory.util.api.response.ApiResponse;
import io.vertx.core.http.HttpServerRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.MultivaluedMap;
import liquibase.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerRequestFilter;
import org.jboss.resteasy.reactive.server.ServerResponseFilter;

import java.lang.reflect.Method;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class ResourceDataEnhancer
{
    private final ApiPaginationInfo paginationInfo;

    private final ResourceInfo      resourceInfo;
    private final HttpServerRequest request;

    @ServerRequestFilter
    public Optional<RestResponse<Void>> enhanceRequest(ContainerRequestContext context)
    {
        Method resourceMethod = this.resourceInfo.getResourceMethod();
        if (resourceMethod.isAnnotationPresent(ApiAction.class))
        {
            String action = resourceMethod.getAnnotation(ApiAction.class).value();
            // TODO: check permission
        }

        try
        {
            MultivaluedMap<String, String> queryParameters = context.getUriInfo().getQueryParameters();
            String size = queryParameters.getFirst("size");
            if (StringUtil.isNumeric(size))
                this.paginationInfo.setSize(Integer.valueOf(size));

            String offset = queryParameters.getFirst("offset");
            if (StringUtil.isNumeric(offset))
                this.paginationInfo.setOffset(Integer.valueOf(offset));

            String includeTotal = queryParameters.getFirst("total");
            if (includeTotal != null)
                this.paginationInfo.includeTotal(includeTotal.equals("true"));
        }
        catch (NumberFormatException ignored)
        {
            // NOOP
        }

        return Optional.empty();
    }

    @ServerResponseFilter
    public void enhanceResponse(ContainerResponseContext context)
    {
        if (!context.hasEntity())
            return;

        if (context.getEntity() instanceof ApiResponse<?> response)
        {
            ApiResourceInfo.Generator info = ApiResourceInfo.builder().href(this.request.absoluteURI());
            this.paginationInfo.getSizeAsLong().ifPresent(info::size);
            this.paginationInfo.getOffsetAsLong().ifPresent(info::offset);
            this.paginationInfo.getTotal().ifPresent(info::total);

            context.setEntity(response.withSelf(info.build()));
        }
    }
}
