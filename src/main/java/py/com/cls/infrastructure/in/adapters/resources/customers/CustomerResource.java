package py.com.cls.infrastructure.in.adapters.resources.customers;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import py.com.cls.application.ports.in.CustomerPort;
import py.com.cls.domain.models.customer.Customer;
import py.com.cls.domain.models.customer.CustomerRequest;
import py.com.cls.infrastructure.in.adapters.errors.ErrorResponse;

@Path("/customers")
@Tag(name = "CustomerResource")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {
    @Inject
    CustomerPort customerPort;
    @Operation(summary = "Customers", description = "Get Customers")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Ok",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(type = SchemaType.ARRAY, implementation = Customer.class)))
    })
    @GET
    public Response getCustomers(
            @QueryParam("page") @DefaultValue("0") int pageIndex,
            @QueryParam("size") @DefaultValue("20") int pageSize
    ) {
        return customerPort.listAll(
                CustomerRequest.builder()
                        .page(pageIndex)
                        .pageSize(pageSize)
                        .build()
        ).fold(
                error -> Response.status(error.getHttpStatusCode())
                        .entity(ErrorResponse.builder().message(error.getMessage()).errorCode(error.getHttpStatusCode()))
                        .build(),
                success -> Response.ok(success).build()
        );
    }
}
