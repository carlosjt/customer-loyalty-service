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
import py.com.cls.infrastructure.out.repository.point_rules.entity.PointsRules;
import py.com.cls.infrastructure.out.repository.point_rules.mapper.PointsRulesMapper;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(value = Transactional.TxType.REQUIRED)
public class PointsRulesRepositoryPortImpl implements PointRuleRepositoryPort {
    @Inject
    PointsRulesRespository pointsRulesRespository;
    @Override
    public Try<Page<PointRule>> listAll(int pageIndex, int pageSize) {
        return Try.of(() -> PageRequest.of(pageIndex, pageSize))
                .map(pageable -> pointsRulesRespository.findAll(pageable))
                .map(PointsRulesMapper.INSTANCE::mapToDomainList)
                .flatMap(pageable -> Try.of(pageable::getData))
                .onFailure(ex -> {
                    log.error("Error getting point rule {}", ex.getMessage(), ex);
                    throw new ApplicationException(ex.getMessage(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
                });
    }
    @Override
    public Try<PointRule> create(final PointRule pointRule) {
        return Try.of(() -> PointsRulesMapper.INSTANCE.toEntity(pointRule))
                .map(entity -> pointsRulesRespository.save(entity))
                .map(PointsRulesMapper.INSTANCE::toDomain)
                .onFailure(ex -> {
                    log.error("Error created point rule {}", ex.getMessage(), ex);
                    throw new ApplicationException(ex.getMessage(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
                });
    }
    @Override
    public Try<PointRule> update(final Integer id, final PointRule pointRule) {
        return Try.of(() -> pointsRulesRespository.findById(id))
                .flatMap(optionalEntity -> {
                    if(optionalEntity.isPresent()){
                        PointsRules entity = optionalEntity.get();
                        final LocalDateTime createdAt = entity.getCreatedAt();
                        PointsRulesMapper.INSTANCE.mapDomainToEntity(pointRule, entity);
                        return Try.of(() -> pointsRulesRespository.save(entity))
                                .flatMap(entitySaved -> {
                                    entitySaved.setCreatedAt(createdAt);
                                    entitySaved.setUpdatedAt(LocalDateTime.now());
                                    return Try.of(() -> entitySaved);
                                });
                    } else {
                        return Try.failure(new ApplicationException("Point rule not found with id: " + id, HttpResponseStatus.NOT_FOUND.code()));
                    }
                })
                .map(PointsRulesMapper.INSTANCE::toDomain)
                .onFailure(ex -> {
                    log.error("Error update point rule {}", ex.getMessage(), ex);
                    throw new ApplicationException(ex.getMessage(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
                });
    }
    @Override
    public Try<PointRule> delete(final Integer id) {
        return Try.of(() -> pointsRulesRespository.findById(id))
                .flatMap(optionalEntity -> {
                    if(optionalEntity.isPresent()){
                        PointsRules entity = optionalEntity.get();
                        return Try.run(() -> pointsRulesRespository.delete(entity))
                                .map(ignored -> PointsRulesMapper.INSTANCE.toDomain(entity));
                    } else {
                        return Try.failure(new ApplicationException("Point rule not found with id: " + id, HttpResponseStatus.NOT_FOUND.code()));
                    }
                })
                .onFailure(ex -> {
                    log.error("Error delete point rule {}", ex.getMessage(), ex);
                    throw new ApplicationException(ex.getMessage(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
                });
    }
}
