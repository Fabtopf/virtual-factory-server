package de.cybine.factory.api.v1.poi;

import de.cybine.factory.data.poi.PointOfInterest;
import de.cybine.factory.service.PointOfInterestService;
import de.cybine.quarkus.api.response.ApiResponse;
import de.cybine.quarkus.util.api.ApiQueryHelper;
import de.cybine.quarkus.util.api.query.ApiCountInfo;
import de.cybine.quarkus.util.api.query.ApiCountQuery;
import de.cybine.quarkus.util.api.query.ApiOptionQuery;
import de.cybine.quarkus.util.api.query.ApiQuery;
import io.quarkus.security.Authenticated;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
public class PointOfInterestResource implements PointOfInterestApi
{
    private final PointOfInterestService service;

    @Override
    @Authenticated
    public RestResponse<ApiResponse<List<PointOfInterest>>> fetch(ApiQuery query)
    {
        return ApiResponse.<List<PointOfInterest>>builder()
                          .value(this.service.fetch(query))
                          .build()
                          .transform(ApiQueryHelper::createResponse);
    }

    @Override
    @Authenticated
    public RestResponse<ApiResponse<PointOfInterest>> fetchSingle(ApiQuery query)
    {
        return ApiResponse.<PointOfInterest>builder()
                          .value(this.service.fetchSingle(query).orElseThrow())
                          .build()
                          .transform(ApiQueryHelper::createResponse);
    }

    @Override
    @Authenticated
    public RestResponse<ApiResponse<List<ApiCountInfo>>> fetchCount(ApiCountQuery query)
    {
        return ApiResponse.<List<ApiCountInfo>>builder()
                          .value(this.service.fetchTotal(query))
                          .build()
                          .transform(ApiQueryHelper::createResponse);
    }

    @Override
    @Authenticated
    public RestResponse<ApiResponse<List<Object>>> fetchOptions(ApiOptionQuery query)
    {
        return ApiResponse.<List<Object>>builder()
                          .value(this.service.fetchOptions(query))
                          .build()
                          .transform(ApiQueryHelper::createResponse);
    }
}
