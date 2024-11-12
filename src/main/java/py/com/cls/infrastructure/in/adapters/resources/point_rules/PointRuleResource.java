package py.com.cls.infrastructure.in.adapters.resources.point_rules;

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
import py.com.cls.application.ports.in.PointRulePort;
import py.com.cls.domain.models.point_rule.PointRule;
import py.com.cls.domain.models.point_rule.PointRuleRequest;
import py.com.cls.infrastructure.in.adapters.exceptions.errors.ErrorResponse;

@Path("/point-rule")
@Tag(name = "PointRuleResource")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PointRuleResource {
    @Inject
    PointRulePort pointRulePort;
    @Operation(summary = "Point Rules", description = "Get Point Rules")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Ok",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(type = SchemaType.ARRAY, implementation = PointRule.class)))
    })
    @GET
    public Response getPointRules(
            @QueryParam("page") @DefaultValue("0") int pageIndex,
            @QueryParam("size") @DefaultValue("20") int pageSize
    ) {
        return pointRulePort.listAll(
                PointRuleRequest.builder()
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
    @Operation(summary = "Point Rules", description = "Create Point Rules")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Ok",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = PointRule.class)))
    })
    @POST
    public Response createPointRules(@Valid @RequestBody final PointRule request) {
        return pointRulePort.create(request).fold(
                error -> Response.status(error.getHttpStatusCode())
                        .entity(ErrorResponse.builder().message(error.getMessage()).errorCode(error.getHttpStatusCode()).build())
                        .build(),
                success -> Response.ok(success).build()
        );
    }
    @Operation(summary = "Point Rules", description = "Update Point Rules")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Ok",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = PointRule.class)))
    })
    @PUT
    @Path("{id}")
    public Response updatePointRules(@PathParam("id") final Integer id,
                                     @Valid @RequestBody final PointRule request) {
        return pointRulePort.update(id, request).fold(
                error -> Response.status(error.getHttpStatusCode())
                        .entity(ErrorResponse.builder().message(error.getMessage()).errorCode(error.getHttpStatusCode()).build())
                        .build(),
                success -> Response.ok(success).build()
        );
    }
    @Operation(summary = "Point Rules", description = "Delete Point Rules")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Ok",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = PointRule.class)))
    })
    @DELETE
    @Path("{id}")
    public Response deletePointRules(@PathParam("id") final Integer id) {
        return pointRulePort.delete(id).fold(
                error -> Response.status(error.getHttpStatusCode())
                        .entity(ErrorResponse.builder().message(error.getMessage()).errorCode(error.getHttpStatusCode()).build())
                        .build(),
                success -> Response.ok(success).build()
        );
    }
}
