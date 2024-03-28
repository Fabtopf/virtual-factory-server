package de.cybine.factory.api.v1.sensor;

import de.cybine.factory.data.sensor.Sensor;
import de.cybine.factory.data.sensor.SensorId;
import de.cybine.factory.service.sensor.SensorEventData;
import de.cybine.factory.service.sensor.SensorService;
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
import java.util.Map;

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
                          .transform(ApiQueryHelper::createResponse);
    }

    @Override
    public RestResponse<ApiResponse<Sensor>> fetchByReferenceId(String referenceId)
    {
        return ApiResponse.<Sensor>builder()
                          .value(this.service.fetchByReferenceId(referenceId).orElseThrow())
                          .build()
                          .transform(ApiQueryHelper::createResponse);
    }

    @Override
    public RestResponse<ApiResponse<Void>> processEvent(String referenceId, String action, Map<String, Object> data)
    {
        this.service.processEvent(SensorEventData.builder().referenceId(referenceId).action(action).data(data).build());
        return ApiResponse.<Void>builder().build().transform(ApiQueryHelper::createResponse);
    }

    @Override
    public RestResponse<ApiResponse<String>> startRecording(String referenceId)
    {
        return ApiResponse.<String>builder()
                          .value(this.service.startEventRecording(referenceId))
                          .build()
                          .transform(ApiQueryHelper::createResponse);
    }

    @Override
    public RestResponse<ApiResponse<Void>> stopRecording(String referenceId)
    {
        this.service.stopEventRecording(referenceId);
        return ApiResponse.<Void>builder().build().transform(ApiQueryHelper::createResponse);
    }

    @Override
    public RestResponse<ApiResponse<List<Sensor>>> fetch(ApiQuery query)
    {
        return ApiResponse.<List<Sensor>>builder()
                          .value(this.service.fetch(query))
                          .build()
                          .transform(ApiQueryHelper::createResponse);
    }

    @Override
    public RestResponse<ApiResponse<Sensor>> fetchSingle(ApiQuery query)
    {
        return ApiResponse.<Sensor>builder()
                          .value(this.service.fetchSingle(query).orElseThrow())
                          .build()
                          .transform(ApiQueryHelper::createResponse);
    }

    @Override
    public RestResponse<ApiResponse<List<ApiCountInfo>>> fetchCount(ApiCountQuery query)
    {
        return ApiResponse.<List<ApiCountInfo>>builder()
                          .value(this.service.fetchTotal(query))
                          .build()
                          .transform(ApiQueryHelper::createResponse);
    }

    @Override
    public RestResponse<ApiResponse<List<Object>>> fetchOptions(ApiOptionQuery query)
    {
        return ApiResponse.<List<Object>>builder()
                          .value(this.service.fetchOptions(query))
                          .build()
                          .transform(ApiQueryHelper::createResponse);
    }
}
