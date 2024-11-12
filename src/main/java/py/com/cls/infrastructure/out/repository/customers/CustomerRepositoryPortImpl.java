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
import py.com.cls.infrastructure.out.repository.customers.mapper.CustomerMapper;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(value = Transactional.TxType.REQUIRED)
public class CustomerRepositoryPortImpl implements CustomerRepositoryPort {
    @Inject
    CustomerRespository customerRespository;
    @Override
    public Try<Page<Customer>> listAll(int pageIndex, int pageSize) {
        return Try.of(() -> PageRequest.of(pageIndex, pageSize))
                .map(pageable -> customerRespository.findAll(pageable))
                .map(CustomerMapper.INSTANCE::mapToResponseList)
                .flatMap(pageable -> Try.of(pageable::getData))
                .onFailure(ex -> {
                    log.error("Error getting customers {}", ex.getMessage(), ex);
                    throw new ApplicationException(ex.getMessage(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
                });
    }
}
