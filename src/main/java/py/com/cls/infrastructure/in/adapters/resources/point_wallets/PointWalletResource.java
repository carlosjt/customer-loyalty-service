package py.com.cls.infrastructure.in.adapters.resources.point_wallets;

import io.netty.handler.codec.http.HttpResponseStatus;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
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
import py.com.cls.application.ports.in.PointWalletPort;
import py.com.cls.domain.models.point_rule.PointRule;
import py.com.cls.domain.models.point_rule.PointRuleRequest;
import py.com.cls.domain.models.point_wallets.PointWalletLoad;
import py.com.cls.domain.models.point_wallets.PointWalletRedemption;
import py.com.cls.infrastructure.in.adapters.exceptions.errors.ErrorResponse;

import java.math.BigDecimal;

@Path("/point-wallet")
@Tag(name = "PointWalletResource")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PointWalletResource {
    @Inject
    PointWalletPort pointWalletPort;
    @Operation(summary = "Point Wallet", description = "Get Points Equivalent")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Ok",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = Integer.class)))
    })
    @GET
    @Path("equivalent")
    public Response getEquivalentPoints(
            @QueryParam("amount") final BigDecimal amount
    ) {
        if (amount == null) {
            return Response.status(HttpResponseStatus.BAD_REQUEST.code())
                    .entity(ErrorResponse.builder().message("Amount query parameter is required.").errorCode(HttpResponseStatus.BAD_REQUEST.code()).build())
                    .build();
        }
        return pointWalletPort.getEquivalentPoints(amount).fold(
                error -> Response.status(error.getHttpStatusCode())
                        .entity(ErrorResponse.builder().message(error.getMessage()).errorCode(error.getHttpStatusCode()).build())
                        .build(),
                success -> Response.ok(success).build()
        );
    }
    @Operation(summary = "Point Wallet", description = "Point Wallet credit to Customer")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Ok",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = PointRule.class)))
    })
    @POST
    @Path("credit")
    public Response creditPoints(@Valid @RequestBody final PointWalletLoad request) {
        return pointWalletPort.creditPoints(request).fold(
                error -> Response.status(error.getHttpStatusCode())
                        .entity(ErrorResponse.builder().message(error.getMessage()).errorCode(error.getHttpStatusCode()).build())
                        .build(),
                success -> Response.ok(success).build()
        );
    }
    @Operation(summary = "Point Wallet", description = "Point Wallet debit to Customer")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Ok",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = PointRule.class)))
    })
    @POST
    @Path("debit")
    public Response debitPoints(@Valid @RequestBody final PointWalletRedemption request) {
        return pointWalletPort.debitPoints(request).fold(
                error -> Response.status(error.getHttpStatusCode())
                        .entity(ErrorResponse.builder().message(error.getMessage()).errorCode(error.getHttpStatusCode()).build())
                        .build(),
                success -> Response.ok(success).build()
        );
    }
}
