package py.com.cls.application.ports.out;

import io.vavr.control.Try;
import org.springframework.data.domain.Page;
import py.com.cls.domain.models.point_concept.PointConcept;

public interface PointConceptRepositoryPort {
    Try<Page<PointConcept>> listAll(final int pageIndex, final int pageSize);
    Try<PointConcept> create(final PointConcept pointConcept);
    Try<PointConcept> update(final Integer id, final PointConcept pointConcept);
    Try<PointConcept> delete(final Integer id);
    Try<PointConcept> findById(final Integer id);
}
