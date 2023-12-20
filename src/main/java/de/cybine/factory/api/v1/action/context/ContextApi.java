package de.cybine.factory.api.v1.action.context;

import de.cybine.factory.data.action.context.ActionContext;
import de.cybine.factory.util.api.query.ApiCountInfo;
import de.cybine.factory.util.api.query.ApiCountQuery;
import de.cybine.factory.util.api.query.ApiOptionQuery;
import de.cybine.factory.util.api.query.ApiQuery;
import de.cybine.factory.util.api.response.ApiResponse;
import io.quarkus.security.Authenticated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;
import java.util.UUID;

@Authenticated
@Path("/api/v1/action/context")
@Tag(name = "ActionContext Resource")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ContextApi
{
    @GET
    @Path("/find/id/{id}")
    RestResponse<ApiResponse<ActionContext>> fetchById(@PathParam("id") UUID id);

    @GET
    @Path("/find/correlation-id/{correlation-id}")
    RestResponse<ApiResponse<ActionContext>> fetchByCorrelationId(@PathParam("correlation-id") String correlationId);

    @POST
    RestResponse<ApiResponse<List<ActionContext>>> fetch(@Valid @NotNull ApiQuery query);

    @POST
    @Path("find")
    RestResponse<ApiResponse<ActionContext>> fetchSingle(@Valid @NotNull ApiQuery query);

    @POST
    @Path("count")
    RestResponse<ApiResponse<List<ApiCountInfo>>> fetchCount(@Valid @NotNull ApiCountQuery query);

    @POST
    @Path("options")
    RestResponse<ApiResponse<List<Object>>> fetchOptions(@Valid @NotNull ApiOptionQuery query);
}