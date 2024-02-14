package de.cybine.factory.api.v1.action.process;

import de.cybine.factory.data.action.process.ActionProcess;
import de.cybine.factory.data.action.process.ActionProcessId;
import de.cybine.factory.service.action.ProcessService;
import de.cybine.quarkus.util.api.query.ApiCountInfo;
import de.cybine.quarkus.util.api.query.ApiCountQuery;
import de.cybine.quarkus.util.api.query.ApiOptionQuery;
import de.cybine.quarkus.util.api.query.ApiQuery;
import de.cybine.quarkus.util.api.response.ApiResponse;
import de.cybine.quarkus.util.cloudevent.CloudEvent;
import io.quarkus.security.Authenticated;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;
import java.util.UUID;

@Authenticated
@ApplicationScoped
@RequiredArgsConstructor
public class ProcessResource implements ProcessApi
{
    private final ProcessService service;

    @Override
    public RestResponse<ApiResponse<ActionProcess>> fetchById(UUID id)
    {
        return ApiResponse.<ActionProcess>builder()
                          .value(this.service.fetchById(ActionProcessId.of(id)).orElseThrow())
                          .build()
                          .toResponse();
    }

    @Override
    public RestResponse<ApiResponse<ActionProcess>> fetchByEventId(String eventId)
    {
        return ApiResponse.<ActionProcess>builder()
                          .value(this.service.fetchByEventId(eventId).orElseThrow())
                          .build()
                          .toResponse();
    }

    @Override
    public RestResponse<ApiResponse<List<ActionProcess>>> fetchByCorrelationId(String correlationId)
    {
        return ApiResponse.<List<ActionProcess>>builder()
                          .value(this.service.fetchByCorrelationId(correlationId))
                          .build()
                          .toResponse();
    }

    @Override
    public RestResponse<ApiResponse<CloudEvent>> fetchCloudEventByEventId(String eventId)
    {
        return ApiResponse.<CloudEvent>builder()
                          .value(this.service.fetchAsCloudEventByEventId(eventId).orElseThrow())
                          .build()
                          .toResponse();
    }

    @Override
    public RestResponse<ApiResponse<List<CloudEvent>>> fetchCloudEventsByCorrelationId(String correlationId)
    {
        return ApiResponse.<List<CloudEvent>>builder()
                          .value(this.service.fetchAsCloudEventsByCorrelationId(correlationId))
                          .build()
                          .toResponse();
    }

    @Override
    public RestResponse<ApiResponse<List<ActionProcess>>> fetch(ApiQuery query)
    {
        return ApiResponse.<List<ActionProcess>>builder().value(this.service.fetch(query)).build().toResponse();
    }

    @Override
    public RestResponse<ApiResponse<ActionProcess>> fetchSingle(ApiQuery query)
    {
        return ApiResponse.<ActionProcess>builder()
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
