package de.cybine.factory.api.v1.poi;

import de.cybine.factory.data.poi.PointOfInterest;
import de.cybine.quarkus.api.response.ApiResponse;
import de.cybine.quarkus.util.api.query.ApiCountInfo;
import de.cybine.quarkus.util.api.query.ApiCountQuery;
import de.cybine.quarkus.util.api.query.ApiOptionQuery;
import de.cybine.quarkus.util.api.query.ApiQuery;
import io.quarkus.security.Authenticated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;

@Path("/api/v1/poi")
@Tag(name = "POI Resource")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface PointOfInterestApi
{
    @POST
    @Authenticated
    RestResponse<ApiResponse<List<PointOfInterest>>> fetch(@Valid @NotNull ApiQuery query);

    @POST
    @Authenticated
    @Path("find")
    RestResponse<ApiResponse<PointOfInterest>> fetchSingle(@Valid @NotNull ApiQuery query);

    @POST
    @Authenticated
    @Path("count")
    RestResponse<ApiResponse<List<ApiCountInfo>>> fetchCount(@Valid @NotNull ApiCountQuery query);

    @POST
    @Authenticated
    @Path("options")
    RestResponse<ApiResponse<List<Object>>> fetchOptions(@Valid @NotNull ApiOptionQuery query);
}
