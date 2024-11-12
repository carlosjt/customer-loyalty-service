package py.com.cls.application.ports.in;

import io.vavr.control.Either;
import io.vavr.control.Try;
import org.springframework.data.domain.Page;
import py.com.cls.application.exceptions.ApplicationException;
import py.com.cls.domain.models.customer.Customer;
import py.com.cls.domain.models.customer.CustomerRequest;
import py.com.cls.domain.models.customer.CustomerResponse;

import java.time.LocalDate;
import java.util.List;

public interface CustomerPort {
    Either<ApplicationException, CustomerResponse<Page<Customer>>> listAll(final CustomerRequest request);
    Either<ApplicationException, CustomerResponse<Customer>> create(final Customer request);
    Either<ApplicationException, CustomerResponse<Customer>> update(final Integer id, final Customer request);
    Either<ApplicationException, CustomerResponse<Customer>> delete(final Integer id);
    Either<ApplicationException, CustomerResponse<List<Customer>>> searchCustomers(final String firstName, final String lastName, final LocalDate birthDate);
}
