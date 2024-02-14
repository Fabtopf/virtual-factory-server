package de.cybine.factory.api.v1.action.context;

import de.cybine.factory.data.action.context.ActionContext;
import de.cybine.factory.data.action.context.ActionContextId;
import de.cybine.factory.service.action.ContextService;
import de.cybine.quarkus.util.api.query.ApiCountInfo;
import de.cybine.quarkus.util.api.query.ApiCountQuery;
import de.cybine.quarkus.util.api.query.ApiOptionQuery;
import de.cybine.quarkus.util.api.query.ApiQuery;
import de.cybine.quarkus.util.api.response.ApiResponse;
import io.quarkus.security.Authenticated;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;
import java.util.UUID;

@Authenticated
@ApplicationScoped
@RequiredArgsConstructor
public class ContextResource implements ContextApi
{
    private final ContextService service;

    @Override
    public RestResponse<ApiResponse<ActionContext>> fetchById(UUID id)
    {
        return ApiResponse.<ActionContext>builder()
                          .value(this.service.fetchById(ActionContextId.of(id)).orElseThrow())
                          .build()
                          .toResponse();
    }

    @Override
    public RestResponse<ApiResponse<ActionContext>> fetchByCorrelationId(String correlationId)
    {
        return ApiResponse.<ActionContext>builder()
                          .value(this.service.fetchByCorrelationId(correlationId).orElseThrow())
                          .build()
                          .toResponse();
    }

    @Override
    public RestResponse<ApiResponse<List<ActionContext>>> fetch(ApiQuery query)
    {
        return ApiResponse.<List<ActionContext>>builder().value(this.service.fetch(query)).build().toResponse();
    }

    @Override
    public RestResponse<ApiResponse<ActionContext>> fetchSingle(ApiQuery query)
    {
        return ApiResponse.<ActionContext>builder()
                          .value(this.service.fetchSingle(query).orElseThrow())
                          .build()
                          .toResponse();
    }

    @Override
    public RestResponse<ApiResponse<List<ApiCountInfo>>> fetchCount(ApiCountQuery query)
    {
        return ApiResponse.<List<ApiCountInfo>>builder().value(this.service.fetchTotal(query)).build().toResponse();
    }

    @Override
    public RestResponse<ApiResponse<List<Object>>> fetchOptions(ApiOptionQuery query)
    {
        return ApiResponse.<List<Object>>builder().value(this.service.fetchOptions(query)).build().toResponse();
    }
}
