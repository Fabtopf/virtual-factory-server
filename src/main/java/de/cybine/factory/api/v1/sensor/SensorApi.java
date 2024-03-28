package de.cybine.factory.api.v1.sensor;

import de.cybine.factory.data.sensor.Sensor;
import de.cybine.quarkus.api.response.ApiResponse;
import de.cybine.quarkus.util.api.query.ApiCountInfo;
import de.cybine.quarkus.util.api.query.ApiCountQuery;
import de.cybine.quarkus.util.api.query.ApiOptionQuery;
import de.cybine.quarkus.util.api.query.ApiQuery;
import io.quarkus.security.Authenticated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;
import java.util.Map;

@Authenticated
@Path("/api/v1/sensor")
@Tag(name = "Sensor Resource")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface SensorApi
{
    @GET
    @Path("/find/id/{id}")
    RestResponse<ApiResponse<Sensor>> fetchById(@PathParam("id") @Min(1) long id);

    @GET
    @Path("/find/reference-id/{reference-id}")
    RestResponse<ApiResponse<Sensor>> fetchByReferenceId(@PathParam("reference-id") String referenceId);

    @POST
    @Path("/event/{reference-id}")
    RestResponse<ApiResponse<Void>> processEvent(@PathParam("reference-id") String referenceId,
            @QueryParam("action") @NotNull String action, @RequestBody Map<String, Object> data);

    @POST
    @Path("/event/{reference-id}/recording/start")
    RestResponse<ApiResponse<String>> startRecording(@PathParam("reference-id") String referenceId);

    @POST
    @Path("/event/{reference-id}/recording/stop")
    RestResponse<ApiResponse<Void>> stopRecording(@PathParam("reference-id") String referenceId);

    @POST
    RestResponse<ApiResponse<List<Sensor>>> fetch(@Valid @NotNull ApiQuery query);

    @POST
    @Path("find")
    RestResponse<ApiResponse<Sensor>> fetchSingle(@Valid @NotNull ApiQuery query);

    @POST
    @Path("count")
    RestResponse<ApiResponse<List<ApiCountInfo>>> fetchCount(@Valid @NotNull ApiCountQuery query);

    @POST
    @Path("options")
    RestResponse<ApiResponse<List<Object>>> fetchOptions(@Valid @NotNull ApiOptionQuery query);
}
