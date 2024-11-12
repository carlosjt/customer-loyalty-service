package py.com.cls.infrastructure.in.adapters.resources.point_concepts;

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
import py.com.cls.application.ports.in.PointConceptPort;
import py.com.cls.domain.models.point_concept.PointConcept;
import py.com.cls.domain.models.point_concept.PointConceptRequest;
import py.com.cls.infrastructure.in.adapters.exceptions.errors.ErrorResponse;

@Path("/point-concept")
@Tag(name = "PointConceptResource")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PointConceptResource {
    @Inject
    PointConceptPort pointConceptPort;
    @Operation(summary = "Point Concept", description = "Get Point Concept")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Ok",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(type = SchemaType.ARRAY, implementation = PointConcept.class)))
    })
    @GET
    public Response getPointConcept(
            @QueryParam("page") @DefaultValue("0") int pageIndex,
            @QueryParam("size") @DefaultValue("20") int pageSize
    ) {
        return pointConceptPort.listAll(
                PointConceptRequest.builder()
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
    @Operation(summary = "Point Concept", description = "Create Point Concept")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Ok",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = PointConcept.class)))
    })
    @POST
    public Response createPointConcept(@Valid @RequestBody final PointConcept request) {
        return pointConceptPort.create(request).fold(
                error -> Response.status(error.getHttpStatusCode())
                        .entity(ErrorResponse.builder().message(error.getMessage()).errorCode(error.getHttpStatusCode()).build())
                        .build(),
                success -> Response.ok(success).build()
        );
    }
    @Operation(summary = "Point Concept", description = "Update Point Concept")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Ok",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = PointConcept.class)))
    })
    @PUT
    @Path("{id}")
    public Response updatePointConcept(@PathParam("id") final Integer id,
                                     @Valid @RequestBody final PointConcept request) {
        return pointConceptPort.update(id, request).fold(
                error -> Response.status(error.getHttpStatusCode())
                        .entity(ErrorResponse.builder().message(error.getMessage()).errorCode(error.getHttpStatusCode()).build())
                        .build(),
                success -> Response.ok(success).build()
        );
    }
    @Operation(summary = "Point Concept", description = "Delete Point Concept")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Ok",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = PointConcept.class)))
    })
    @DELETE
    @Path("{id}")
    public Response deletePointConcept(@PathParam("id") final Integer id) {
        return pointConceptPort.delete(id).fold(
                error -> Response.status(error.getHttpStatusCode())
                        .entity(ErrorResponse.builder().message(error.getMessage()).errorCode(error.getHttpStatusCode()).build())
                        .build(),
                success -> Response.ok(success).build()
        );
    }
}
