package py.com.cls.application.ports.in;

import io.vavr.control.Either;
import org.springframework.data.domain.Page;
import py.com.cls.application.exceptions.ApplicationException;
import py.com.cls.domain.models.point_rule.PointRule;
import py.com.cls.domain.models.point_rule.PointRuleRequest;
import py.com.cls.domain.models.point_rule.PointRuleResponse;

public interface PointRulePort {
    Either<ApplicationException, PointRuleResponse<Page<PointRule>>> listAll(final PointRuleRequest request);
    Either<ApplicationException, PointRuleResponse<PointRule>> create(final PointRule request);
    Either<ApplicationException, PointRuleResponse<PointRule>> update(final Integer id, final PointRule request);
    Either<ApplicationException, PointRuleResponse<PointRule>> delete(final Integer id);
}
