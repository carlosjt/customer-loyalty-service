package py.com.cls.infrastructure.out.repository.point_wallets;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vavr.control.Try;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import py.com.cls.application.exceptions.ApplicationException;
import py.com.cls.application.ports.out.PointWalletRepositoryPort;
import py.com.cls.domain.models.point_wallets.PointWallet;
import py.com.cls.infrastructure.out.repository.point_wallets.entity.PointsWallet;
import py.com.cls.infrastructure.out.repository.point_wallets.mapper.PointsWalletMapper;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(value = Transactional.TxType.REQUIRED)
public class PointsWalletRepositoryPortImpl implements PointWalletRepositoryPort {
    @Inject
    PointsWalletRepository pointsWalletRepository;
    @Override
    public Try<PointWallet> create(final PointWallet pointWallet) {
        return Try.of(() -> PointsWalletMapper.INSTANCE.toEntity(pointWallet))
                .map(entity -> pointsWalletRepository.save(entity))
                .map(PointsWalletMapper.INSTANCE::toDomain)
                .onFailure(ex -> {
                    log.error("Error created point wallet {}", ex.getMessage(), ex);
                    if(!(ex instanceof ApplicationException)) {
                        throw new ApplicationException(ex.getMessage(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
                    }
                });
    }
    @Override
    public Try<PointWallet> update(final Integer id, final PointWallet pointWallet) {
        return Try.of(() -> pointsWalletRepository.findById(id))
                .flatMap(optionalEntity -> {
                    if(optionalEntity.isPresent()){
                        PointsWallet entity = optionalEntity.get();
                        final LocalDateTime createdAt = entity.getCreatedAt();
                        PointsWalletMapper.INSTANCE.mapDomainToEntity(pointWallet, entity);
                        return Try.of(() -> pointsWalletRepository.save(entity))
                                .flatMap(entitySaved -> {
                                    entitySaved.setCreatedAt(createdAt);
                                    entitySaved.setUpdatedAt(LocalDateTime.now());
                                    return Try.of(() -> entitySaved);
                                });
                    } else {
                        return Try.failure(new ApplicationException("Point wallet not found with id: " + id, HttpResponseStatus.NOT_FOUND.code()));
                    }
                })
                .map(PointsWalletMapper.INSTANCE::toDomain)
                .onFailure(ex -> {
                    log.error("Error update point wallet {}", ex.getMessage(), ex);
                    if(!(ex instanceof ApplicationException)) {
                        throw new ApplicationException(ex.getMessage(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
                    }
                });
    }
    @Override
    public Try<List<PointWallet>> findByCustomerAndBalancePointsGreaterThan(final Integer id, final Integer balanceThreshold) {
        return Try.of(() -> pointsWalletRepository.findByCustomer_IdAndBalancePointsGreaterThanOrderByCreatedAtAsc(id, balanceThreshold))
                .map(pointsWallets -> pointsWallets.stream()
                        .map(PointsWalletMapper.INSTANCE::toDomain)
                        .toList())
                .onFailure(ex -> {
                    log.error("Error fetching point wallets for customer with ID {}: {}", id, ex.getMessage(), ex);
                    throw new ApplicationException("Error fetching point wallets", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
                })
                .filter(list -> !list.isEmpty())
                .orElse(Try.failure(new ApplicationException("No point wallets found for customer ID: " + id, HttpResponseStatus.NOT_FOUND.code())));
    }
}
