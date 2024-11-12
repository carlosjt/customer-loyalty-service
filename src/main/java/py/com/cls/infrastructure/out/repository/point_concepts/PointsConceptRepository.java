package py.com.cls.infrastructure.out.repository.point_concepts;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import py.com.cls.infrastructure.out.repository.point_concepts.entity.PointsConcept;

@Repository
public interface PointsConceptRepository extends CrudRepository<PointsConcept, Integer> {
    Page<PointsConcept> findAll(Pageable pageable);
}
