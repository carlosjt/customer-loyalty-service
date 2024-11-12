package py.com.cls.infrastructure.out.repository.point_rules;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import py.com.cls.infrastructure.out.repository.point_rules.entity.PointsRule;

import java.math.BigDecimal;

@Repository
public interface PointsRuleRepository extends CrudRepository<PointsRule, Integer> {
    Page<PointsRule> findAll(final Pageable pageable);
    @Query("SELECT r FROM PointsRule r WHERE :amount >= r.lowerLimit AND (:amount <= r.upperLimit OR r.upperLimit IS NULL) ORDER BY r.lowerLimit DESC")
    PointsRule findApplicableRule(@Param("amount") final BigDecimal amount);
}
