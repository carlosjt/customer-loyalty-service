package py.com.cls.application.ports.in;

import io.vavr.control.Either;
import org.springframework.data.domain.Page;
import py.com.cls.application.exceptions.ApplicationException;
import py.com.cls.domain.models.point_concept.PointConcept;
import py.com.cls.domain.models.point_concept.PointConceptRequest;
import py.com.cls.domain.models.point_concept.PointConceptResponse;

public interface PointConceptPort {
    Either<ApplicationException, PointConceptResponse<Page<PointConcept>>> listAll(final PointConceptRequest request);
    Either<ApplicationException, PointConceptResponse<PointConcept>> create(final PointConcept request);
    Either<ApplicationException, PointConceptResponse<PointConcept>> update(final Integer id, final PointConcept request);
    Either<ApplicationException, PointConceptResponse<PointConcept>> delete(final Integer id);
}
