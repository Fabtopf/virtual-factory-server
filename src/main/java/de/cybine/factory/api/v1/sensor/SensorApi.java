package de.cybine.factory.api.v1.sensor;

import de.cybine.factory.data.sensor.Sensor;
import de.cybine.quarkus.util.api.query.ApiCountInfo;
import de.cybine.quarkus.util.api.query.ApiCountQuery;
import de.cybine.quarkus.util.api.query.ApiOptionQuery;
import de.cybine.quarkus.util.api.query.ApiQuery;
import de.cybine.quarkus.util.api.response.ApiResponse;
import io.quarkus.security.Authenticated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;

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
    RestResponse<ApiResponse<Sensor>> fetchByReferenceId(@PathParam("reference-id") String correlationId);

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
