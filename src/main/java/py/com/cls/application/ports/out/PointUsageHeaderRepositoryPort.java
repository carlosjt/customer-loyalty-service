package py.com.cls.application.ports.out;

import io.vavr.control.Try;
import py.com.cls.domain.models.point_usage_headers.PointUsageHeader;

public interface PointUsageHeaderRepositoryPort {
    Try<PointUsageHeader> create(final PointUsageHeader pointUsageHeader);
}

