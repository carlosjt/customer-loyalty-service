package py.com.cls.application.ports.out;

import io.vavr.control.Try;
import org.springframework.data.domain.Page;
import py.com.cls.domain.models.customer.Customer;

public interface CustomerRepositoryPort {
    Try<Page<Customer>> listAll(final int pageIndex, final int pageSize);
}
