package py.com.cls.application.ports.out;

import io.vavr.control.Try;
import org.springframework.data.domain.Page;
import py.com.cls.domain.models.customer.Customer;

import java.time.LocalDate;
import java.util.List;

public interface CustomerRepositoryPort {
    Try<Page<Customer>> listAll(final int pageIndex, final int pageSize);
    Try<Customer> create(final Customer customer);
    Try<Customer> update(final Integer id, final Customer customer);
    Try<Customer> delete(final Integer id);
    Try<Customer> findById(final Integer id);
    Try<List<Customer>> searchCustomers(final String firstName, final String lastName, final LocalDate birthDate);
}
