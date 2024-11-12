package py.com.cls.infrastructure.out.repository.point_concepts;

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
import py.com.cls.application.ports.out.PointConceptRepositoryPort;
import py.com.cls.domain.models.point_concept.PointConcept;
import py.com.cls.infrastructure.out.repository.point_concepts.entity.PointsConcept;
import py.com.cls.infrastructure.out.repository.point_concepts.mapper.PointsConceptMapper;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(value = Transactional.TxType.REQUIRED)
public class PointsConceptRepositoryPortImpl implements PointConceptRepositoryPort {
    @Inject
    PointsConceptRepository pointsConceptRepository;
    @Override
    public Try<Page<PointConcept>> listAll(final int pageIndex, final int pageSize) {
        return Try.of(() -> PageRequest.of(pageIndex, pageSize))
                .map(pageable -> pointsConceptRepository.findAll(pageable))
                .map(PointsConceptMapper.INSTANCE::mapToDomainList)
                .flatMap(pageable -> Try.of(pageable::getData))
                .onFailure(ex -> {
                    log.error("Error getting point concept {}", ex.getMessage(), ex);
                    if(!(ex instanceof ApplicationException)) {
                        throw new ApplicationException(ex.getMessage(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
                    }
                });
    }
    @Override
    public Try<PointConcept> create(final PointConcept pointConcept) {
        return Try.of(() -> PointsConceptMapper.INSTANCE.toEntity(pointConcept))
                .map(entity -> pointsConceptRepository.save(entity))
                .map(PointsConceptMapper.INSTANCE::toDomain)
                .onFailure(ex -> {
                    log.error("Error created point concept {}", ex.getMessage(), ex);
                    if(!(ex instanceof ApplicationException)) {
                        throw new ApplicationException(ex.getMessage(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
                    }
                });
    }
    @Override
    public Try<PointConcept> update(final Integer id, final PointConcept pointConcept) {
        return Try.of(() -> pointsConceptRepository.findById(id))
                .flatMap(optionalEntity -> {
                    if(optionalEntity.isPresent()){
                        PointsConcept entity = optionalEntity.get();
                        final LocalDateTime createdAt = entity.getCreatedAt();
                        PointsConceptMapper.INSTANCE.mapDomainToEntity(pointConcept, entity);
                        return Try.of(() -> pointsConceptRepository.save(entity))
                                .flatMap(entitySaved -> {
                                    entitySaved.setCreatedAt(createdAt);
                                    entitySaved.setUpdatedAt(LocalDateTime.now());
                                    return Try.of(() -> entitySaved);
                                });
                    } else {
                        return Try.failure(new ApplicationException("Point concept not found with id: " + id, HttpResponseStatus.NOT_FOUND.code()));
                    }
                })
                .map(PointsConceptMapper.INSTANCE::toDomain)
                .onFailure(ex -> {
                    log.error("Error update point concept {}", ex.getMessage(), ex);
                    if(!(ex instanceof ApplicationException)) {
                        throw new ApplicationException(ex.getMessage(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
                    }
                });
    }
    @Override
    public Try<PointConcept> delete(final Integer id) {
        return Try.of(() -> pointsConceptRepository.findById(id))
                .flatMap(optionalEntity -> {
                    if(optionalEntity.isPresent()){
                        PointsConcept entity = optionalEntity.get();
                        return Try.run(() -> pointsConceptRepository.delete(entity))
                                .map(ignored -> PointsConceptMapper.INSTANCE.toDomain(entity));
                    } else {
                        return Try.failure(new ApplicationException("Point concept not found with id: " + id, HttpResponseStatus.NOT_FOUND.code()));
                    }
                })
                .onFailure(ex -> {
                    log.error("Error delete point concept {}", ex.getMessage(), ex);
                    if(!(ex instanceof ApplicationException)) {
                        throw new ApplicationException(ex.getMessage(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
                    }
                });
    }

    @Override
    public Try<PointConcept> findById(final Integer id) {
        return Try.of(() -> pointsConceptRepository.findById(id))
                .flatMap(optionalEntity -> {
                    if(optionalEntity.isPresent()){
                        PointsConcept entity = optionalEntity.get();
                        return Try.of(() -> PointsConceptMapper.INSTANCE.toDomain(entity));
                    } else {
                        return Try.failure(new ApplicationException("Point concept not found with id: " + id, HttpResponseStatus.NOT_FOUND.code()));
                    }
                })
                .onFailure(ex -> {
                    log.error("Error get point concept {}", ex.getMessage(), ex);
                    if(!(ex instanceof ApplicationException)) {
                        throw new ApplicationException(ex.getMessage(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
                    }
                });
    }
}
