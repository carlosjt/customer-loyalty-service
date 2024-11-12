package py.com.cls.infrastructure.out.repository.point_rules;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import py.com.cls.infrastructure.out.repository.point_rules.entity.PointsRules;

@Repository
public interface PointsRulesRespository extends CrudRepository<PointsRules, Integer> {
    Page<PointsRules> findAll(Pageable pageable);
}
