package py.com.cls.infrastructure.out.repository.customers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import py.com.cls.infrastructure.out.repository.customers.entity.Customers;

@Repository
public interface CustomerRespository extends CrudRepository<Customers, Long> {
    Page<Customers> findAll(Pageable pageable);
}
