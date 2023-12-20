package de.cybine.factory.api.v1.action.process;

import de.cybine.factory.data.action.process.ActionProcess;
import de.cybine.factory.util.api.query.ApiCountInfo;
import de.cybine.factory.util.api.query.ApiCountQuery;
import de.cybine.factory.util.api.query.ApiOptionQuery;
import de.cybine.factory.util.api.query.ApiQuery;
import de.cybine.factory.util.api.response.ApiResponse;
import de.cybine.factory.util.cloudevent.CloudEvent;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;
import java.util.UUID;

@Path("/api/v1/action/process")
@Tag(name = "ActionProcess Resource")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ProcessApi
{
    @GET
    @Path("/find/id/{id}")
    RestResponse<ApiResponse<ActionProcess>> fetchById(@PathParam("id") UUID id);

    @GET
    @Path("/find/event-id/{event-id}")
    RestResponse<ApiResponse<ActionProcess>> fetchByEventId(@PathParam("event-id") String eventId);

    @GET
    @Path("/find/correlation-id/{correlation-id}")
    RestResponse<ApiResponse<List<ActionProcess>>> fetchByCorrelationId(
            @PathParam("correlation-id") String correlationId);

    @GET
    @Path("/cloud-event/event-id/{event-id}")
    RestResponse<ApiResponse<CloudEvent>> fetchCloudEventByEventId(@PathParam("event-id") String eventId);

    @GET
    @Path("/cloud-event/correlation-id/{correlation-id}")
    RestResponse<ApiResponse<List<CloudEvent>>> fetchCloudEventsByCorrelationId(
            @PathParam("correlation-id") String correlationId);

    @POST
    RestResponse<ApiResponse<List<ActionProcess>>> fetch(@Valid @NotNull ApiQuery query);

    @POST
    @Path("find")
    RestResponse<ApiResponse<ActionProcess>> fetchSingle(@Valid @NotNull ApiQuery query);

    @POST
    @Path("count")
    RestResponse<ApiResponse<List<ApiCountInfo>>> fetchCount(@Valid @NotNull ApiCountQuery query);

    @POST
    @Path("options")
    RestResponse<ApiResponse<List<Object>>> fetchOptions(@Valid @NotNull ApiOptionQuery query);
}
