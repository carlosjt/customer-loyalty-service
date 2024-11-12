package py.com.cls.infrastructure.in.adapters.resources.point_expirations;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import py.com.cls.application.ports.in.PointExpirationPort;
import py.com.cls.domain.models.point_expiration.PointExpiration;
import py.com.cls.domain.models.point_expiration.PointExpirationRequest;
import py.com.cls.infrastructure.in.adapters.exceptions.errors.ErrorResponse;

@Path("/point-expiration")
@Tag(name = "PointExpirationResource")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PointExpirationResource {
    @Inject
    PointExpirationPort pointExpirationPort;
    @Operation(summary = "Point Expiration", description = "Get Point Expiration")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Ok",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(type = SchemaType.ARRAY, implementation = PointExpiration.class)))
    })
    @GET
    public Response getPointExpiration(
            @QueryParam("page") @DefaultValue("0") int pageIndex,
            @QueryParam("size") @DefaultValue("20") int pageSize
    ) {
        return pointExpirationPort.listAll(
                PointExpirationRequest.builder()
                        .page(pageIndex)
                        .pageSize(pageSize)
                        .build()
        ).fold(
                error -> Response.status(error.getHttpStatusCode())
                        .entity(ErrorResponse.builder().message(error.getMessage()).errorCode(error.getHttpStatusCode()).build())
                        .build(),
                success -> Response.ok(success).build()
        );
    }
    @Operation(summary = "Point Expiration", description = "Create Point Expiration")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Ok",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = PointExpiration.class)))
    })
    @POST
    public Response createPointExpiration(@Valid @RequestBody final PointExpiration request) {
        return pointExpirationPort.create(request).fold(
                error -> Response.status(error.getHttpStatusCode())
                        .entity(ErrorResponse.builder().message(error.getMessage()).errorCode(error.getHttpStatusCode()).build())
                        .build(),
                success -> Response.ok(success).build()
        );
    }
    @Operation(summary = "Point Expiration", description = "Update Point Expiration")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Ok",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = PointExpiration.class)))
    })
    @PUT
    @Path("{id}")
    public Response updatePointExpiration(@PathParam("id") final Integer id,
                                     @Valid @RequestBody final PointExpiration request) {
        return pointExpirationPort.update(id, request).fold(
                error -> Response.status(error.getHttpStatusCode())
                        .entity(ErrorResponse.builder().message(error.getMessage()).errorCode(error.getHttpStatusCode()).build())
                        .build(),
                success -> Response.ok(success).build()
        );
    }
    @Operation(summary = "Point Expiration", description = "Delete Point Expiration")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Ok",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = PointExpiration.class)))
    })
    @DELETE
    @Path("{id}")
    public Response deletePointExpiration(@PathParam("id") final Integer id) {
        return pointExpirationPort.delete(id).fold(
                error -> Response.status(error.getHttpStatusCode())
                        .entity(ErrorResponse.builder().message(error.getMessage()).errorCode(error.getHttpStatusCode()).build())
                        .build(),
                success -> Response.ok(success).build()
        );
    }
}
