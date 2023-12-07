package de.cybine.factory.api.v1.action.metadata;

import de.cybine.factory.data.action.metadata.ActionMetadata;
import de.cybine.factory.data.action.metadata.ActionMetadataId;
import de.cybine.factory.service.action.MetadataService;
import de.cybine.factory.util.api.query.ApiCountInfo;
import de.cybine.factory.util.api.query.ApiCountQuery;
import de.cybine.factory.util.api.query.ApiOptionQuery;
import de.cybine.factory.util.api.query.ApiQuery;
import de.cybine.factory.util.api.response.ApiResponse;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
public class MetadataResource implements MetadataApi
{
    private final MetadataService service;

    @Override
    public RestResponse<ApiResponse<ActionMetadata>> fetchById(long id)
    {
        return ApiResponse.<ActionMetadata>builder()
                          .value(this.service.fetchById(ActionMetadataId.of(id)).orElseThrow())
                          .build()
                          .toResponse();
    }

    @Override
    public RestResponse<ApiResponse<List<ActionMetadata>>> fetch(ApiQuery query)
    {
        return ApiResponse.<List<ActionMetadata>>builder().value(this.service.fetch(query)).build().toResponse();
    }

    @Override
    public RestResponse<ApiResponse<ActionMetadata>> fetchSingle(ApiQuery query)
    {
        return ApiResponse.<ActionMetadata>builder()
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
