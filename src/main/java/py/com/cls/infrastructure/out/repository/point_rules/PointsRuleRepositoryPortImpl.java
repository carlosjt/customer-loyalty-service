package py.com.cls.infrastructure.out.repository.point_rules;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vavr.control.Try;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import py.com.cls.application.exceptions.ApplicationException;
import py.com.cls.application.ports.out.PointRuleRepositoryPort;
import py.com.cls.domain.models.point_rule.PointRule;
import py.com.cls.infrastructure.out.repository.point_rules.entity.PointsRule;
import py.com.cls.infrastructure.out.repository.point_rules.mapper.PointsRuleMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(value = Transactional.TxType.REQUIRED)
public class PointsRuleRepositoryPortImpl implements PointRuleRepositoryPort {
    @Inject
    PointsRuleRepository pointsRuleRepository;
    @Override
    public Try<Page<PointRule>> listAll(final int pageIndex, final int pageSize) {
        return Try.of(() -> PageRequest.of(pageIndex, pageSize))
                .map(pageable -> pointsRuleRepository.findAll(pageable))
                .map(PointsRuleMapper.INSTANCE::mapToDomainList)
                .flatMap(pageable -> Try.of(pageable::getData))
                .onFailure(ex -> {
                    log.error("Error getting point rule {}", ex.getMessage(), ex);
                    if(!(ex instanceof ApplicationException)) {
                        throw new ApplicationException(ex.getMessage(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
                    }
                });
    }
    @Override
    public Try<PointRule> create(final PointRule pointRule) {
        return Try.of(() -> PointsRuleMapper.INSTANCE.toEntity(pointRule))
                .map(entity -> pointsRuleRepository.save(entity))
                .map(PointsRuleMapper.INSTANCE::toDomain)
                .onFailure(ex -> {
                    log.error("Error created point rule {}", ex.getMessage(), ex);
                    if(!(ex instanceof ApplicationException)) {
                        throw new ApplicationException(ex.getMessage(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
                    }
                });
    }
    @Override
    public Try<PointRule> update(final Integer id, final PointRule pointRule) {
        return Try.of(() -> pointsRuleRepository.findById(id))
                .flatMap(optionalEntity -> {
                    if(optionalEntity.isPresent()){
                        PointsRule entity = optionalEntity.get();
                        final LocalDateTime createdAt = entity.getCreatedAt();
                        PointsRuleMapper.INSTANCE.mapDomainToEntity(pointRule, entity);
                        return Try.of(() -> pointsRuleRepository.save(entity))
                                .flatMap(entitySaved -> {
                                    entitySaved.setCreatedAt(createdAt);
                                    entitySaved.setUpdatedAt(LocalDateTime.now());
                                    return Try.of(() -> entitySaved);
                                });
                    } else {
                        return Try.failure(new ApplicationException("Point rule not found with id: " + id, HttpResponseStatus.NOT_FOUND.code()));
                    }
                })
                .map(PointsRuleMapper.INSTANCE::toDomain)
                .onFailure(ex -> {
                    log.error("Error update point rule {}", ex.getMessage(), ex);
                    if(!(ex instanceof ApplicationException)) {
                        throw new ApplicationException(ex.getMessage(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
                    }
                });
    }
    @Override
    public Try<PointRule> delete(final Integer id) {
        return Try.of(() -> pointsRuleRepository.findById(id))
                .flatMap(optionalEntity -> {
                    if(optionalEntity.isPresent()){
                        PointsRule entity = optionalEntity.get();
                        return Try.run(() -> pointsRuleRepository.delete(entity))
                                .map(ignored -> PointsRuleMapper.INSTANCE.toDomain(entity));
                    } else {
                        return Try.failure(new ApplicationException("Point rule not found with id: " + id, HttpResponseStatus.NOT_FOUND.code()));
                    }
                })
                .onFailure(ex -> {
                    log.error("Error delete point rule {}", ex.getMessage(), ex);
                    if(!(ex instanceof ApplicationException)) {
                        throw new ApplicationException(ex.getMessage(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
                    }
                });
    }
    @Override
    public Try<PointRule> findApplicableRule(BigDecimal amount) {
        return Try.of(() -> pointsRuleRepository.findApplicableRule(amount))
                .flatMap(optionalEntity -> Try.of(() -> optionalEntity)
                        .map(PointsRuleMapper.INSTANCE::toDomain));
    }
    @Override
    public Try<Integer> findPointWithAmount(final BigDecimal amount) {
        return Try.of(() -> pointsRuleRepository.findApplicableRule(amount))
                .flatMap(optionalRule -> Try.of(() -> calculatePoints(amount, optionalRule))
                        .filter(points -> points >= 0)
                        .onFailure(ex -> {
                            log.error("Error calculating points {}", ex.getMessage(), ex);
                            if(!(ex instanceof ApplicationException)) {
                                throw new ApplicationException(ex.getMessage(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
                            }
                        })
                );
    }
    private Integer calculatePoints(final BigDecimal amount, final PointsRule rule) {
        BigDecimal pointsEquivalent = amount.divide(rule.getEquivalenceAmount(), BigDecimal.ROUND_DOWN)
                .multiply(BigDecimal.valueOf(rule.getPointsAwarded()));
        return pointsEquivalent.intValue();
    }
}
