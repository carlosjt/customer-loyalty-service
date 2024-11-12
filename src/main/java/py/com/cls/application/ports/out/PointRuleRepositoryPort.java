package py.com.cls.application.ports.out;

import io.vavr.control.Try;
import org.springframework.data.domain.Page;
import py.com.cls.domain.models.point_rule.PointRule;

public interface PointRuleRepositoryPort {
    Try<Page<PointRule>> listAll(final int pageIndex, final int pageSize);
    Try<PointRule> create(final PointRule pointRule);
    Try<PointRule> update(final Integer id, final PointRule pointRule);
    Try<PointRule> delete(final Integer id);
}
