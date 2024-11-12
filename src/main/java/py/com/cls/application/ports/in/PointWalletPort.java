package py.com.cls.application.ports.in;

import io.vavr.control.Either;
import py.com.cls.application.exceptions.ApplicationException;
import py.com.cls.domain.models.point_rule.PointRule;
import py.com.cls.domain.models.point_rule.PointRuleResponse;
import py.com.cls.domain.models.point_usage_summary.PointUsageSummary;
import py.com.cls.domain.models.point_wallets.PointWallet;
import py.com.cls.domain.models.point_wallets.PointWalletLoad;
import py.com.cls.domain.models.point_wallets.PointWalletRedemption;
import py.com.cls.domain.models.point_wallets.PointWalletResponse;

import java.math.BigDecimal;

public interface PointWalletPort {
    Either<ApplicationException, PointWalletResponse<PointWallet>> creditPoints(final PointWalletLoad request);
    Either<ApplicationException, PointWalletResponse<PointUsageSummary>> debitPoints(final PointWalletRedemption request);
    Either<ApplicationException, PointWalletResponse<Integer>> getEquivalentPoints(final BigDecimal amount);
}

