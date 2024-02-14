package de.cybine.factory.api.v1.sensor;

import de.cybine.factory.data.sensor.Sensor;
import de.cybine.factory.data.sensor.SensorId;
import de.cybine.factory.service.sensor.SensorService;
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

@Authenticated
@ApplicationScoped
@RequiredArgsConstructor
public class SensorResource implements SensorApi
{
    private final SensorService service;

    @Override
    public RestResponse<ApiResponse<Sensor>> fetchById(long id)
    {
        return ApiResponse.<Sensor>builder()
                          .value(this.service.fetchById(SensorId.of(id)).orElseThrow())
                          .build()
                          .toResponse();
    }

    @Override
    public RestResponse<ApiResponse<Sensor>> fetchByReferenceId(String referenceId)
    {
        return ApiResponse.<Sensor>builder()
                          .value(this.service.fetchByReferenceId(referenceId).orElseThrow())
                          .build()
                          .toResponse();
    }

    @Override
    public RestResponse<ApiResponse<List<Sensor>>> fetch(ApiQuery query)
    {
        return ApiResponse.<List<Sensor>>builder().value(this.service.fetch(query)).build().toResponse();
    }

    @Override
    public RestResponse<ApiResponse<Sensor>> fetchSingle(ApiQuery query)
    {
        return ApiResponse.<Sensor>builder().value(this.service.fetchSingle(query).orElseThrow()).build().toResponse();
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
