package py.com.cls.application.usecases.point_concepts;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vavr.control.Either;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import py.com.cls.application.common.annotations.UseCase;
import py.com.cls.application.exceptions.ApplicationException;
import py.com.cls.application.ports.in.PointConceptPort;
import py.com.cls.application.ports.out.PointConceptRepositoryPort;
import py.com.cls.domain.models.point_concept.PointConcept;
import py.com.cls.domain.models.point_concept.PointConceptRequest;
import py.com.cls.domain.models.point_concept.PointConceptResponse;

@UseCase
@RequiredArgsConstructor
public class PointConceptUseCase implements PointConceptPort {
    private final PointConceptRepositoryPort pointConceptRepositoryPort;
    @Override
    public Either<ApplicationException, PointConceptResponse<Page<PointConcept>>> listAll(final PointConceptRequest request) {
        return getConcepts((PointConceptRequest) request.validate());
    }
    @Override
    public Either<ApplicationException, PointConceptResponse<PointConcept>> create(final PointConcept request) {
        return createConcepts(request);
    }
    @Override
    public Either<ApplicationException, PointConceptResponse<PointConcept>> update(final Integer id, final PointConcept request) {
        return updateConcepts(id, request);
    }
    @Override
    public Either<ApplicationException, PointConceptResponse<PointConcept>> delete(final Integer id) {
        return deleteConcepts(id);
    }
    private Either<ApplicationException, PointConceptResponse<Page<PointConcept>>> getConcepts(final PointConceptRequest request) {
        return Try.ofCallable(() -> pointConceptRepositoryPort.listAll(request.getPage(), request.getPageSize()))
                .map(response -> PointConceptResponse.<Page<PointConcept>>builder().data(response.get()).build())
                .toEither()
                .mapLeft(this::mapToApplicationException);
    }

    private Either<ApplicationException, PointConceptResponse<PointConcept>> createConcepts(final PointConcept request) {
        return Try.ofCallable(() -> pointConceptRepositoryPort.create(request))
                .map(response -> PointConceptResponse.<PointConcept>builder().data(response.get()).build())
                .toEither()
                .mapLeft(this::mapToApplicationException);
    }
    private Either<ApplicationException, PointConceptResponse<PointConcept>> updateConcepts(final Integer id, final PointConcept request) {
        return Try.ofCallable(() -> pointConceptRepositoryPort.update(id, request))
                .map(response -> PointConceptResponse.<PointConcept>builder().data(response.get()).build())
                .toEither()
                .mapLeft(this::mapToApplicationException);
    }
    private Either<ApplicationException, PointConceptResponse<PointConcept>> deleteConcepts(final Integer id) {
        return Try.ofCallable(() -> pointConceptRepositoryPort.delete(id))
                .map(response -> PointConceptResponse.<PointConcept>builder().data(response.get()).build())
                .toEither()
                .mapLeft(this::mapToApplicationException);
    }
    private ApplicationException mapToApplicationException(final Throwable throwable) {
        if (throwable instanceof ApplicationException) {
            return (ApplicationException) throwable;
        }
        return new ApplicationException("Unexpected error customers", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
    }
}
