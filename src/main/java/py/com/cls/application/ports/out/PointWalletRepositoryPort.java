package py.com.cls.application.ports.out;

import io.vavr.control.Try;
import py.com.cls.domain.models.point_wallets.PointWallet;

import java.util.List;

public interface PointWalletRepositoryPort {
    Try<PointWallet> create(final PointWallet pointWallet);
    Try<PointWallet> update(final Integer id, final PointWallet pointWallet);
    Try<List<PointWallet>> findByCustomerAndBalancePointsGreaterThan(final Integer id, final Integer balanceThreshold);
}

