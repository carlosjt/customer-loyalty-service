package py.com.cls.application.ports.out;

import io.vavr.control.Try;
import py.com.cls.domain.models.point_usage_details.PointUsageDetail;

import java.util.List;

public interface PointUsageDetailRepositoryPort {
    Try<PointUsageDetail> create(final PointUsageDetail pointUsageDetail);
    Try<List<PointUsageDetail>> createAll(final List<PointUsageDetail> pointUsageDetail);
}

