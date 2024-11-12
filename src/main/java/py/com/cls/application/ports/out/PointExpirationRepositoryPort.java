package py.com.cls.application.ports.out;

import io.vavr.control.Try;
import org.springframework.data.domain.Page;
import py.com.cls.domain.models.point_expiration.PointExpiration;

import java.time.LocalDate;

public interface PointExpirationRepositoryPort {
    Try<Page<PointExpiration>> listAll(final int pageIndex, final int pageSize);
    Try<PointExpiration> create(final PointExpiration pointExpiration);
    Try<PointExpiration> update(final Integer id, final PointExpiration pointExpiration);
    Try<PointExpiration> delete(final Integer id);
    Try<PointExpiration> findByDateRange(final LocalDate date);
}
