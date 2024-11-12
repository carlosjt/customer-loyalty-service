package py.com.cls.application.usecases.point_rules;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vavr.control.Either;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import py.com.cls.application.common.annotations.UseCase;
import py.com.cls.application.exceptions.ApplicationException;
import py.com.cls.application.ports.in.PointRulePort;
import py.com.cls.application.ports.out.PointRuleRepositoryPort;
import py.com.cls.domain.models.point_rule.PointRule;
import py.com.cls.domain.models.point_rule.PointRuleRequest;
import py.com.cls.domain.models.point_rule.PointRuleResponse;

@UseCase
@RequiredArgsConstructor
public class PointRuleUseCase implements PointRulePort {
    private final PointRuleRepositoryPort pointRuleRepositoryPort;
    @Override
    public Either<ApplicationException, PointRuleResponse<Page<PointRule>>> listAll(final PointRuleRequest request) {
        return getPointsRules((PointRuleRequest) request.validate());
    }
    @Override
    public Either<ApplicationException, PointRuleResponse<PointRule>> create(final PointRule request) {
        return createPointsRules(request);
    }
    @Override
    public Either<ApplicationException, PointRuleResponse<PointRule>> update(final Integer id, final PointRule request) {
        return updatePointsRules(id, request);
    }
    @Override
    public Either<ApplicationException, PointRuleResponse<PointRule>> delete(final Integer id) {
        return deletePointsRules(id);
    }
    private Either<ApplicationException, PointRuleResponse<Page<PointRule>>> getPointsRules(final PointRuleRequest request) {
        return Try.ofCallable(() -> pointRuleRepositoryPort.listAll(request.getPage(), request.getPageSize()))
                .map(response -> PointRuleResponse.<Page<PointRule>>builder().data(response.get()).build())
                .toEither()
                .mapLeft(this::mapToApplicationException);
    }
    private Either<ApplicationException, PointRuleResponse<PointRule>> createPointsRules(final PointRule request) {
        return Try.ofCallable(() -> pointRuleRepositoryPort.create(request))
                .map(response -> PointRuleResponse.<PointRule>builder().data(response.get()).build())
                .toEither()
                .mapLeft(this::mapToApplicationException);
    }
    private Either<ApplicationException, PointRuleResponse<PointRule>> updatePointsRules(final Integer id, final PointRule request) {
        return Try.ofCallable(() -> pointRuleRepositoryPort.update(id, request))
                .map(response -> PointRuleResponse.<PointRule>builder().data(response.get()).build())
                .toEither()
                .mapLeft(this::mapToApplicationException);
    }
    private Either<ApplicationException, PointRuleResponse<PointRule>> deletePointsRules(final Integer id) {
        return Try.ofCallable(() -> pointRuleRepositoryPort.delete(id))
                .map(response -> PointRuleResponse.<PointRule>builder().data(response.get()).build())
                .toEither()
                .mapLeft(this::mapToApplicationException);
    }
    private ApplicationException mapToApplicationException(final Throwable throwable) {
        if (throwable instanceof ApplicationException) {
            return (ApplicationException) throwable;
        }
        return new ApplicationException("Enexpected error points rules", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
    }
}
