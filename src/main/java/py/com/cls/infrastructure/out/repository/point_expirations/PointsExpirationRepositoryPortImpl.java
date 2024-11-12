package py.com.cls.infrastructure.out.repository.point_expirations;

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
import py.com.cls.application.ports.out.PointExpirationRepositoryPort;
import py.com.cls.domain.models.point_expiration.PointExpiration;
import py.com.cls.infrastructure.out.repository.point_expirations.entity.PointsExpiration;
import py.com.cls.infrastructure.out.repository.point_expirations.mapper.PointsExpirationMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(value = Transactional.TxType.REQUIRED)
public class PointsExpirationRepositoryPortImpl implements PointExpirationRepositoryPort {
    @Inject
    PointsExpirationRepository pointsExpirationRepository;
    @Override
    public Try<Page<PointExpiration>> listAll(final int pageIndex, final int pageSize) {
        return Try.of(() -> PageRequest.of(pageIndex, pageSize))
                .map(pageable -> pointsExpirationRepository.findAll(pageable))
                .map(PointsExpirationMapper.INSTANCE::mapToDomainList)
                .flatMap(pageable -> Try.of(pageable::getData))
                .onFailure(ex -> {
                    log.error("Error getting point expiration {}", ex.getMessage(), ex);
                    if(!(ex instanceof ApplicationException)) {
                        throw new ApplicationException(ex.getMessage(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
                    }
                });
    }
    @Override
    public Try<PointExpiration> create(final PointExpiration pointExpiration) {
        return Try.of(() -> PointsExpirationMapper.INSTANCE.toEntity(pointExpiration))
                .map(entity -> {
                    final LocalDate startDate = pointExpiration.getStartDate();
                    final LocalDate endDate = pointExpiration.getEndDate();
                    calculateDaysBetween(startDate, endDate)
                            .onSuccess(days -> entity.setDurationDays(Math.toIntExact(days)))
                            .getOrElseThrow(PointsExpirationRepositoryPortImpl::ThrowableCheck);
                    return entity;
                })
                .map(entity -> pointsExpirationRepository.save(entity))
                .map(PointsExpirationMapper.INSTANCE::toDomain)
                .onFailure(ex -> {
                    log.error("Error created point expiration {}", ex.getMessage(), ex);
                    if(!(ex instanceof ApplicationException)) {
                        throw new ApplicationException(ex.getMessage(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
                    }
                });
    }
    @Override
    public Try<PointExpiration> update(final Integer id, final PointExpiration pointExpiration) {
        return Try.of(() -> pointsExpirationRepository.findById(id))
                .flatMap(optionalEntity -> {
                    if(optionalEntity.isPresent()){
                        PointsExpiration entity = optionalEntity.get();
                        final LocalDateTime createdAt = entity.getCreatedAt();
                        final LocalDate startDate = pointExpiration.getStartDate();
                        final LocalDate endDate = pointExpiration.getEndDate();
                        PointsExpirationMapper.INSTANCE.mapDomainToEntity(pointExpiration, entity);
                        calculateDaysBetween(startDate, endDate)
                                .onSuccess(days -> entity.setDurationDays(Math.toIntExact(days)))
                                .getOrElseThrow(PointsExpirationRepositoryPortImpl::ThrowableCheck);
                        return Try.of(() -> pointsExpirationRepository.save(entity))
                                .flatMap(entitySaved -> {
                                    entitySaved.setCreatedAt(createdAt);
                                    entitySaved.setUpdatedAt(LocalDateTime.now());
                                    return Try.of(() -> entitySaved);
                                });
                    } else {
                        return Try.failure(new ApplicationException("Point expiration not found with id: " + id, HttpResponseStatus.NOT_FOUND.code()));
                    }
                })
                .map(PointsExpirationMapper.INSTANCE::toDomain)
                .onFailure(ex -> {
                    log.error("Error update point expiration {}", ex.getMessage(), ex);
                    if(!(ex instanceof ApplicationException)) {
                        throw new ApplicationException(ex.getMessage(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
                    }
                });
    }
    @Override
    public Try<PointExpiration> delete(final Integer id) {
        return Try.of(() -> pointsExpirationRepository.findById(id))
                .flatMap(optionalEntity -> {
                    if(optionalEntity.isPresent()){
                        PointsExpiration entity = optionalEntity.get();
                        return Try.run(() -> pointsExpirationRepository.delete(entity))
                                .map(ignored -> PointsExpirationMapper.INSTANCE.toDomain(entity));
                    } else {
                        return Try.failure(new ApplicationException("Point expiration not found with id: " + id, HttpResponseStatus.NOT_FOUND.code()));
                    }
                })
                .onFailure(ex -> {
                    log.error("Error delete point expiration {}", ex.getMessage(), ex);
                    if(!(ex instanceof ApplicationException)) {
                        throw new ApplicationException(ex.getMessage(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
                    }
                });
    }
    @Override
    public Try<PointExpiration> findByDateRange(final LocalDate date) {
        return Try.of(() -> pointsExpirationRepository.findByDateWithinRange(date))
                .map(PointsExpirationMapper.INSTANCE::toDomain);
    }
    public static Try<Long> calculateDaysBetween(final LocalDate startDate, final LocalDate endDate) {
        return Try.of(() -> {
            if (endDate.isBefore(startDate)) {
                throw new ApplicationException("endDate must not be before startDate", HttpResponseStatus.BAD_REQUEST.code());
            }
            return ChronoUnit.DAYS.between(startDate, endDate);
        });
    }
    private static ApplicationException ThrowableCheck(final Throwable ex) {
        if (ex instanceof ApplicationException) {
            return (ApplicationException) ex;
        } else {
            log.error("Unexpected error on point expiration: {}", ex.getMessage(), ex);
            return new ApplicationException(ex.getMessage(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
        }
    }
}
