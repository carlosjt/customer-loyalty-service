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

@UseCase
@RequiredArgsConstructor
public class CustomerUseCase implements CustomerPort {
    private final CustomerRepositoryPort customerRepositoryPort;
    @Override
    public Either<ApplicationException, CustomerResponse<Page<Customer>>> listAll(final CustomerRequest request) {
        return getCustomers((CustomerRequest) request.validate());
    }
    private Either<ApplicationException, CustomerResponse<Page<Customer>>> getCustomers(final CustomerRequest request) {
        return Try.ofCallable(() -> customerRepositoryPort.listAll(request.getPage(), request.getPageSize()))
                .map(response -> CustomerResponse.<Page<Customer>>builder().data(response.get()).build())
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
