package py.com.cls.infrastructure.out.repository.point_usage_headers;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vavr.control.Try;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import py.com.cls.application.exceptions.ApplicationException;
import py.com.cls.application.ports.out.PointUsageHeaderRepositoryPort;
import py.com.cls.domain.models.point_usage_headers.PointUsageHeader;
import py.com.cls.infrastructure.out.repository.point_usage_headers.mapper.PointsUsageHeaderMapper;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(value = Transactional.TxType.REQUIRED)
public class PointsUsageHeaderRepositoryPortImpl implements PointUsageHeaderRepositoryPort {
    @Inject
    PointsUsageHeaderRepository pointsUsageHeaderRepository;
    @Override
    public Try<PointUsageHeader> create(final PointUsageHeader pointUsageHeader) {
        return Try.of(() -> PointsUsageHeaderMapper.INSTANCE.toEntity(pointUsageHeader))
                .map(entity -> pointsUsageHeaderRepository.save(entity))
                .map(PointsUsageHeaderMapper.INSTANCE::toDomain)
                .onFailure(ex -> {
                    log.error("Error created point usage header {}", ex.getMessage(), ex);
                    if(!(ex instanceof ApplicationException)) {
                        throw new ApplicationException(ex.getMessage(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
                    }
                });
    }
}
