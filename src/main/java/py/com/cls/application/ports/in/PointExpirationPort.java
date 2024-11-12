package py.com.cls.application.ports.in;

import io.vavr.control.Either;
import org.springframework.data.domain.Page;
import py.com.cls.application.exceptions.ApplicationException;
import py.com.cls.domain.models.point_expiration.PointExpiration;
import py.com.cls.domain.models.point_expiration.PointExpirationRequest;
import py.com.cls.domain.models.point_expiration.PointExpirationResponse;

import java.time.LocalDate;

public interface PointExpirationPort {
    Either<ApplicationException, PointExpirationResponse<Page<PointExpiration>>> listAll(final PointExpirationRequest request);
    Either<ApplicationException, PointExpirationResponse<PointExpiration>> create(final PointExpiration request);
    Either<ApplicationException, PointExpirationResponse<PointExpiration>> update(final Integer id, final PointExpiration request);
    Either<ApplicationException, PointExpirationResponse<PointExpiration>> delete(final Integer id);
    Either<ApplicationException, PointExpirationResponse<PointExpiration>> findByDateRange(final LocalDate date);
}
