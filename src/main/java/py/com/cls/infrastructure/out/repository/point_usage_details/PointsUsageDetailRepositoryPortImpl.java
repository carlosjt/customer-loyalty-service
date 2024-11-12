package py.com.cls.infrastructure.out.repository.point_usage_details;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vavr.control.Try;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import py.com.cls.application.exceptions.ApplicationException;
import py.com.cls.application.ports.out.PointUsageDetailRepositoryPort;
import py.com.cls.domain.models.point_usage_details.PointUsageDetail;
import py.com.cls.infrastructure.out.repository.point_usage_details.mapper.PointsUsageDetailMapper;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(value = Transactional.TxType.REQUIRED)
public class PointsUsageDetailRepositoryPortImpl implements PointUsageDetailRepositoryPort {
    @Inject
    PointsUsageDetailRepository pointsUsageDetailRepository;
    @Override
    public Try<PointUsageDetail> create(final PointUsageDetail pointUsageDetail) {
        return Try.of(() -> PointsUsageDetailMapper.INSTANCE.toEntity(pointUsageDetail))
                .map(entity -> pointsUsageDetailRepository.save(entity))
                .map(PointsUsageDetailMapper.INSTANCE::toDomain)
                .onFailure(ex -> {
                    log.error("Error created point detail header {}", ex.getMessage(), ex);
                    if(!(ex instanceof ApplicationException)) {
                        throw new ApplicationException(ex.getMessage(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
                    }
                });
    }
    @Override
    public Try<List<PointUsageDetail>> createAll(final List<PointUsageDetail> pointUsageDetails) {
        final List<PointUsageDetail> createdDetails = new ArrayList<>();
        for (PointUsageDetail detail : pointUsageDetails) {
            final Try<PointUsageDetail> result = Try.of(() -> PointsUsageDetailMapper.INSTANCE.toEntity(detail))
                    .map(entity -> pointsUsageDetailRepository.save(entity))
                    .map(PointsUsageDetailMapper.INSTANCE::toDomain)
                    .onFailure(ex -> {
                        log.error("Error creating point detail for header {}", ex.getMessage(), ex);
                        if (!(ex instanceof ApplicationException)) {
                            throw new ApplicationException(ex.getMessage(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
                        }
                    });
            if (result.isFailure()) {
                return Try.failure(result.getCause());
            }
            createdDetails.add(result.get());
        }
        return Try.success(createdDetails);
    }
}
