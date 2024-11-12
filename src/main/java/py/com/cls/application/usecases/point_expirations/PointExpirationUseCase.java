package py.com.cls.application.usecases.point_expirations;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vavr.control.Either;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import py.com.cls.application.common.annotations.UseCase;
import py.com.cls.application.exceptions.ApplicationException;
import py.com.cls.application.ports.in.PointExpirationPort;
import py.com.cls.application.ports.out.PointExpirationRepositoryPort;
import py.com.cls.domain.models.point_expiration.PointExpiration;
import py.com.cls.domain.models.point_expiration.PointExpirationRequest;
import py.com.cls.domain.models.point_expiration.PointExpirationResponse;

import java.time.LocalDate;

@UseCase
@RequiredArgsConstructor
public class PointExpirationUseCase implements PointExpirationPort {
    private final PointExpirationRepositoryPort pointExpirationRepositoryPort;
    @Override
    public Either<ApplicationException, PointExpirationResponse<Page<PointExpiration>>> listAll(final PointExpirationRequest request) {
        return getPointsExpiration((PointExpirationRequest) request.validate());
    }
    @Override
    public Either<ApplicationException, PointExpirationResponse<PointExpiration>> create(final PointExpiration request) {
        return createPointsExpiration(request);
    }
    @Override
    public Either<ApplicationException, PointExpirationResponse<PointExpiration>> update(final Integer id, final PointExpiration request) {
        return updatePointsExpiration(id, request);
    }
    @Override
    public Either<ApplicationException, PointExpirationResponse<PointExpiration>> delete(final Integer id) {
        return deletePointsExpiration(id);
    }
    @Override
    public Either<ApplicationException, PointExpirationResponse<PointExpiration>> findByDateRange(final LocalDate date) {
        return getFindByDateRange(date);
    }
    private Either<ApplicationException, PointExpirationResponse<Page<PointExpiration>>> getPointsExpiration(final PointExpirationRequest request) {
        return Try.ofCallable(() -> pointExpirationRepositoryPort.listAll(request.getPage(), request.getPageSize()))
                .map(response -> PointExpirationResponse.<Page<PointExpiration>>builder().data(response.get()).build())
                .toEither()
                .mapLeft(this::mapToApplicationException);
    }
    private Either<ApplicationException, PointExpirationResponse<PointExpiration>> createPointsExpiration(final PointExpiration request) {
        return Try.ofCallable(() -> pointExpirationRepositoryPort.create(request))
                .map(response -> PointExpirationResponse.<PointExpiration>builder().data(response.get()).build())
                .toEither()
                .mapLeft(this::mapToApplicationException);
    }
    private Either<ApplicationException, PointExpirationResponse<PointExpiration>> updatePointsExpiration(final Integer id, final PointExpiration request) {
        return Try.ofCallable(() -> pointExpirationRepositoryPort.update(id, request))
                .map(response -> PointExpirationResponse.<PointExpiration>builder().data(response.get()).build())
                .toEither()
                .mapLeft(this::mapToApplicationException);
    }
    private Either<ApplicationException, PointExpirationResponse<PointExpiration>> deletePointsExpiration(final Integer id) {
        return Try.ofCallable(() -> pointExpirationRepositoryPort.delete(id))
                .map(response -> PointExpirationResponse.<PointExpiration>builder().data(response.get()).build())
                .toEither()
                .mapLeft(this::mapToApplicationException);
    }
    private Either<ApplicationException, PointExpirationResponse<PointExpiration>> getFindByDateRange(final LocalDate date) {
        return Try.ofCallable(() -> pointExpirationRepositoryPort.findByDateRange(date))
                .map(response -> PointExpirationResponse.<PointExpiration>builder().data(response.get()).build())
                .toEither()
                .mapLeft(this::mapToApplicationException);
    }
    private ApplicationException mapToApplicationException(final Throwable throwable) {
        if (throwable instanceof ApplicationException) {
            return (ApplicationException) throwable;
        }
        return new ApplicationException("Enexpected error points expiration", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
    }
}
