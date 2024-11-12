package py.com.cls.infrastructure.out.repository.point_expirations;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import py.com.cls.infrastructure.out.repository.point_expirations.entity.PointsExpiration;

import java.time.LocalDate;

@Repository
public interface PointsExpirationRepository extends CrudRepository<PointsExpiration, Integer> {
    Page<PointsExpiration> findAll(final Pageable pageable);
    @Query("SELECT p FROM PointsExpiration p WHERE :date BETWEEN p.startDate AND COALESCE(p.endDate, :date)")
    PointsExpiration findByDateWithinRange(@Param("date") final LocalDate date);
}
