package py.com.cls.infrastructure.out.repository.customers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import py.com.cls.infrastructure.out.repository.customers.entity.Customers;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CustomerRepository extends CrudRepository<Customers, Integer> {
    Page<Customers> findAll(Pageable pageable);
    @Query("SELECT c FROM Customers c " +
            "WHERE (:firstName IS NULL OR (CAST(c.firstName AS string)) LIKE (CONCAT('%', :firstName, '%'))) " +
            "AND (:lastName IS NULL OR (CAST(c.lastName AS string)) LIKE (CONCAT('%', :lastName, '%'))) " +
            "AND (:birthDate IS NULL OR DATE(c.birthDate) = :birthDate)")
    List<Customers> findByDynamicFilters(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("birthDate") LocalDate birthDate);
}
