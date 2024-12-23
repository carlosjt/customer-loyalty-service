package py.com.cls.infrastructure.in.adapters.resources.customers;

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
import py.com.cls.application.ports.in.CustomerPort;
import py.com.cls.domain.models.customer.Customer;
import py.com.cls.domain.models.customer.CustomerRequest;
import py.com.cls.domain.models.point_rule.PointRule;
import py.com.cls.infrastructure.in.adapters.exceptions.errors.ErrorResponse;

import java.time.LocalDate;

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
    @Operation(summary = "Customers", description = "Create Customers")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Ok",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = Customer.class)))
    })
    @POST
    public Response createCustomers(@Valid @RequestBody final Customer request) {
        return customerPort.create(request).fold(
                error -> Response.status(error.getHttpStatusCode())
                        .entity(ErrorResponse.builder().message(error.getMessage()).errorCode(error.getHttpStatusCode()).build())
                        .build(),
                success -> Response.ok(success).build()
        );
    }
    @Operation(summary = "Customers", description = "Update Customers")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Ok",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = Customer.class)))
    })
    @PUT
    @Path("{id}")
    public Response updateCustomers(@PathParam("id") final Integer id,
                                     @Valid @RequestBody final Customer request) {
        return customerPort.update(id, request).fold(
                error -> Response.status(error.getHttpStatusCode())
                        .entity(ErrorResponse.builder().message(error.getMessage()).errorCode(error.getHttpStatusCode()).build())
                        .build(),
                success -> Response.ok(success).build()
        );
    }
    @Operation(summary = "Customers", description = "Delete Customers")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Ok",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = PointRule.class)))
    })
    @DELETE
    @Path("{id}")
    public Response deleteCustomers(@PathParam("id") final Integer id) {
        return customerPort.delete(id).fold(
                error -> Response.status(error.getHttpStatusCode())
                        .entity(ErrorResponse.builder().message(error.getMessage()).errorCode(error.getHttpStatusCode()).build())
                        .build(),
                success -> Response.ok(success).build()
        );
    }
    @Operation(summary = "Customers", description = "Get Customers With Criteria")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Ok",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(type = SchemaType.ARRAY, implementation = Customer.class)))
    })
    @GET
    @Path("/search")
    public Response getCustomersByCriteria(
            @QueryParam("firstName") @DefaultValue("") final String firstName,
            @QueryParam("lastName") @DefaultValue("") final String lastName,
            @QueryParam("birthDate") final String birthDate
    ) {
        final LocalDate dateParse = birthDate == null || birthDate.isEmpty() ? null : LocalDate.parse(birthDate);
        return customerPort.searchCustomers(
                firstName, lastName, dateParse
        ).fold(
                error -> Response.status(error.getHttpStatusCode())
                        .entity(ErrorResponse.builder().message(error.getMessage()).errorCode(error.getHttpStatusCode()))
                        .build(),
                success -> Response.ok(success).build()
        );
    }
}
