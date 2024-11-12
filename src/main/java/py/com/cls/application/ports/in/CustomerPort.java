package py.com.cls.application.ports.in;

import io.vavr.control.Either;
import org.springframework.data.domain.Page;
import py.com.cls.application.exceptions.ApplicationException;
import py.com.cls.domain.models.customer.Customer;
import py.com.cls.domain.models.customer.CustomerRequest;
import py.com.cls.domain.models.customer.CustomerResponse;

public interface CustomerPort {
    Either<ApplicationException, CustomerResponse<Page<Customer>>> listAll(final CustomerRequest request);
}
