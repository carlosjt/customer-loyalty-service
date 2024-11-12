package py.com.cls.infrastructure.out.repository.customers;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vavr.control.Try;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import py.com.cls.application.exceptions.ApplicationException;
import py.com.cls.application.ports.out.CustomerRepositoryPort;
import py.com.cls.domain.models.customer.Customer;
import py.com.cls.infrastructure.out.repository.customers.entity.Customers;
import py.com.cls.infrastructure.out.repository.customers.mapper.CustomerMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(value = Transactional.TxType.REQUIRED)
public class CustomerRepositoryPortImpl implements CustomerRepositoryPort {
    @Inject
    CustomerRepository customerRepository;
    @Override
    public Try<Page<Customer>> listAll(int pageIndex, int pageSize) {
        return Try.of(() -> PageRequest.of(pageIndex, pageSize))
                .map(pageable -> customerRepository.findAll(pageable))
                .map(CustomerMapper.INSTANCE::mapToDomainList)
                .flatMap(pageable -> Try.of(pageable::getData))
                .onFailure(ex -> {
                    log.error("Error getting customer {}", ex.getMessage(), ex);
                    if(!(ex instanceof ApplicationException)) {
                        throw new ApplicationException(ex.getMessage(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
                    }
                });
    }

    @Override
    public Try<Customer> create(final Customer customer) {
        return Try.of(() -> CustomerMapper.INSTANCE.toEntity(customer))
                .map(entity -> customerRepository.save(entity))
                .map(CustomerMapper.INSTANCE::toDomain)
                .onFailure(ex -> {
                    log.error("Error created customer {}", ex.getMessage(), ex);
                    if(!(ex instanceof ApplicationException)) {
                        throw new ApplicationException(ex.getMessage(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
                    }
                });
    }
    @Override
    public Try<Customer> update(final Integer id, final Customer customer) {
        return Try.of(() -> customerRepository.findById(id))
                .flatMap(optionalEntity -> {
                    if(optionalEntity.isPresent()){
                        Customers entity = optionalEntity.get();
                        final LocalDateTime createdAt = entity.getCreatedAt();
                        CustomerMapper.INSTANCE.mapDomainToEntity(customer, entity);
                        return Try.of(() -> customerRepository.save(entity))
                                .flatMap(entitySaved -> {
                                    entitySaved.setCreatedAt(createdAt);
                                    entitySaved.setUpdatedAt(LocalDateTime.now());
                                    return Try.of(() -> entitySaved);
                                });
                    } else {
                        return Try.failure(new ApplicationException("Customer not found with id: " + id, HttpResponseStatus.NOT_FOUND.code()));
                    }
                })
                .map(CustomerMapper.INSTANCE::toDomain)
                .onFailure(ex -> {
                    log.error("Error update customer {}", ex.getMessage(), ex);
                    if(!(ex instanceof ApplicationException)) {
                        throw new ApplicationException(ex.getMessage(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
                    }
                });
    }
    @Override
    public Try<Customer> delete(final Integer id) {
        return Try.of(() -> customerRepository.findById(id))
                .flatMap(optionalEntity -> {
                    if(optionalEntity.isPresent()){
                        Customers entity = optionalEntity.get();
                        return Try.run(() -> customerRepository.delete(entity))
                                .map(ignored -> CustomerMapper.INSTANCE.toDomain(entity));
                    } else {
                        return Try.failure(new ApplicationException("Customer not found with id: " + id, HttpResponseStatus.NOT_FOUND.code()));
                    }
                })
                .onFailure(ex -> {
                    log.error("Error delete customer {}", ex.getMessage(), ex);
                    if(!(ex instanceof ApplicationException)) {
                        throw new ApplicationException(ex.getMessage(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
                    }
                });
    }

    @Override
    public Try<Customer> findById(final Integer id) {
        return Try.of(() -> customerRepository.findById(id))
                .flatMap(optionalEntity -> {
                    if(optionalEntity.isPresent()){
                        Customers entity = optionalEntity.get();
                        return Try.of(() -> entity)
                                .map(ignored -> CustomerMapper.INSTANCE.toDomain(entity));
                    } else {
                        return Try.failure(new ApplicationException("Customer not found with id: " + id, HttpResponseStatus.NOT_FOUND.code()));
                    }
                })
                .onFailure(ex -> {
                    log.error("Error delete customer {}", ex.getMessage(), ex);
                    if(!(ex instanceof ApplicationException)) {
                        throw new ApplicationException(ex.getMessage(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
                    }
                });
    }

    @Override
    public Try<List<Customer>> searchCustomers(final String firstName, final String lastName, final LocalDate birthDate) {
        return Try.of(() -> customerRepository.findByDynamicFilters(firstName, lastName, birthDate))
                .map(CustomerMapper.INSTANCE::toDomainListWithoutPage)
                .onFailure(ex -> {
                    log.error("Error getting customer with search {}", ex.getMessage(), ex);
                    if(!(ex instanceof ApplicationException)) {
                        throw new ApplicationException(ex.getMessage(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
                    }
                });
    }
}
