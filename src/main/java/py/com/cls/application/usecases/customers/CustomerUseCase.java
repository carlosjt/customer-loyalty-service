package py.com.cls.application.usecases.customers;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vavr.control.Either;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import py.com.cls.application.common.annotations.UseCase;
import py.com.cls.application.exceptions.ApplicationException;
import py.com.cls.application.ports.in.CustomerPort;
import py.com.cls.application.ports.out.CustomerRepositoryPort;
import py.com.cls.domain.models.customer.Customer;
import py.com.cls.domain.models.customer.CustomerRequest;
import py.com.cls.domain.models.customer.CustomerResponse;

import java.time.LocalDate;
import java.util.List;

@UseCase
@RequiredArgsConstructor
public class CustomerUseCase implements CustomerPort {
    private final CustomerRepositoryPort customerRepositoryPort;
    @Override
    public Either<ApplicationException, CustomerResponse<Page<Customer>>> listAll(final CustomerRequest request) {
        return getCustomers((CustomerRequest) request.validate());
    }
    @Override
    public Either<ApplicationException, CustomerResponse<Customer>> create(final Customer request) {
        return createCustomers(request);
    }
    @Override
    public Either<ApplicationException, CustomerResponse<Customer>> update(final Integer id, final Customer request) {
        return updateCustomers(id, request);
    }
    @Override
    public Either<ApplicationException, CustomerResponse<Customer>> delete(final Integer id) {
        return deleteCustomers(id);
    }
    @Override
    public Either<ApplicationException, CustomerResponse<List<Customer>>> searchCustomers(final String firstName, final String lastName, final LocalDate birthDate) {
        return searchCustomer(firstName, lastName, birthDate);
    }
    private Either<ApplicationException, CustomerResponse<List<Customer>>> searchCustomer(final String firstName, final String lastName, final LocalDate birthDate) {
        return Try.ofCallable(() -> customerRepositoryPort.searchCustomers(firstName, lastName, birthDate))
                .map(response -> CustomerResponse.<List<Customer>>builder().data(response.get()).build())
                .toEither()
                .mapLeft(this::mapToApplicationException);
    }
    private Either<ApplicationException, CustomerResponse<Page<Customer>>> getCustomers(final CustomerRequest request) {
        return Try.ofCallable(() -> customerRepositoryPort.listAll(request.getPage(), request.getPageSize()))
                .map(response -> CustomerResponse.<Page<Customer>>builder().data(response.get()).build())
                .toEither()
                .mapLeft(this::mapToApplicationException);
    }
    private Either<ApplicationException, CustomerResponse<Customer>> createCustomers(final Customer request) {
        return Try.ofCallable(() -> customerRepositoryPort.create(request))
                .map(response -> CustomerResponse.<Customer>builder().data(response.get()).build())
                .toEither()
                .mapLeft(this::mapToApplicationException);
    }
    private Either<ApplicationException, CustomerResponse<Customer>> updateCustomers(final Integer id, final Customer request) {
        return Try.ofCallable(() -> customerRepositoryPort.update(id, request))
                .map(response -> CustomerResponse.<Customer>builder().data(response.get()).build())
                .toEither()
                .mapLeft(this::mapToApplicationException);
    }
    private Either<ApplicationException, CustomerResponse<Customer>> deleteCustomers(final Integer id) {
        return Try.ofCallable(() -> customerRepositoryPort.delete(id))
                .map(response -> CustomerResponse.<Customer>builder().data(response.get()).build())
                .toEither()
                .mapLeft(this::mapToApplicationException);
    }
    private ApplicationException mapToApplicationException(final Throwable throwable) {
        if (throwable instanceof ApplicationException) {
            return (ApplicationException) throwable;
        }
        return new ApplicationException("Enexpected error customers", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
    }
}
