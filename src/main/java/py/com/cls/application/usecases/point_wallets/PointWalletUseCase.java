package py.com.cls.application.usecases.point_wallets;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vavr.control.Either;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import py.com.cls.application.common.annotations.UseCase;
import py.com.cls.application.exceptions.ApplicationException;
import py.com.cls.application.ports.in.PointExpirationPort;
import py.com.cls.application.ports.in.PointWalletPort;
import py.com.cls.application.ports.out.CustomerRepositoryPort;
import py.com.cls.application.ports.out.EmailSmtpPort;
import py.com.cls.application.ports.out.PointConceptRepositoryPort;
import py.com.cls.application.ports.out.PointRuleRepositoryPort;
import py.com.cls.application.ports.out.PointUsageDetailRepositoryPort;
import py.com.cls.application.ports.out.PointUsageHeaderRepositoryPort;
import py.com.cls.application.ports.out.PointWalletRepositoryPort;
import py.com.cls.domain.models.customer.Customer;
import py.com.cls.domain.models.point_concept.PointConcept;
import py.com.cls.domain.models.point_expiration.PointExpiration;
import py.com.cls.domain.models.point_expiration.PointExpirationResponse;
import py.com.cls.domain.models.point_rule.PointRule;
import py.com.cls.domain.models.point_usage_details.PointUsageDetail;
import py.com.cls.domain.models.point_usage_headers.PointUsageHeader;
import py.com.cls.domain.models.point_usage_summary.PointUsageSummary;
import py.com.cls.domain.models.point_wallets.PointWallet;
import py.com.cls.domain.models.point_wallets.PointWalletLoad;
import py.com.cls.domain.models.point_wallets.PointWalletRedemption;
import py.com.cls.domain.models.point_wallets.PointWalletResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@UseCase
@RequiredArgsConstructor
public class PointWalletUseCase implements PointWalletPort {
    private final PointRuleRepositoryPort pointRuleRepositoryPort;
    private final CustomerRepositoryPort customerRepositoryPort;
    private final PointExpirationPort pointExpirationPort;
    private final PointWalletRepositoryPort pointWalletRepositoryPort;
    private final PointConceptRepositoryPort pointConceptRepositoryPort;
    private final PointUsageHeaderRepositoryPort pointUsageHeaderRepositoryPort;
    private final PointUsageDetailRepositoryPort pointUsageDetailRepositoryPort;
    private final EmailSmtpPort emailSmtpPort;
    @Override
    public Either<ApplicationException, PointWalletResponse<PointWallet>> creditPoints(final PointWalletLoad request) {
        return creditPoint(request);
    }
    @Override
    public Either<ApplicationException, PointWalletResponse<PointUsageSummary>> debitPoints(final PointWalletRedemption request) {
        return debitPoint(request);
    }
    @Override
    public Either<ApplicationException, PointWalletResponse<Integer>> getEquivalentPoints(final BigDecimal amount) {
        return Try.ofCallable(() -> pointRuleRepositoryPort.findPointWithAmount(amount))
                .map(point -> PointWalletResponse.<Integer>builder().data(point.get()).build())
                .toEither()
                .mapLeft(this::mapToApplicationException);
    }
    private Either<ApplicationException, PointWalletResponse<PointWallet>> creditPoint(final PointWalletLoad pointWalletLoad) {
        return Try.ofCallable(() -> customerRepositoryPort.findById(pointWalletLoad.getCustomerId()))
                .flatMap(customer -> {
                    if(customer.isSuccess()) {
                        final PointRule pointRule = pointRuleRepositoryPort.findApplicableRule(pointWalletLoad.getTransactionAmount())
                                .getOrElseThrow(this::mapToApplicationException);
                        final Integer points = pointRuleRepositoryPort.findPointWithAmount(pointWalletLoad.getTransactionAmount())
                                .getOrElseThrow(this::mapToApplicationException);
                        final PointExpiration pointExpiration = pointExpirationPort.findByDateRange(LocalDate.now())
                                .map(PointExpirationResponse::getData)
                                .getOrElseThrow(this::mapToApplicationException);
                        final PointWallet pointWallet = PointWallet.builder()
                                .customer(customer.get())
                                .pointRule(pointRule)
                                .pointExpiration(pointExpiration)
                                .assignmentDate(LocalDate.now())
                                .assignedPoints(points)
                                .usedPoints(0)
                                .transactionAmount(pointWalletLoad.getTransactionAmount())
                                .build();
                        return Try.ofCallable(() -> pointWalletRepositoryPort.create(pointWallet));
                    } else {
                        throw new ApplicationException(customer.getCause().getMessage(), HttpResponseStatus.NOT_FOUND.code());
                    }
                })
                .map(point -> PointWalletResponse.<PointWallet>builder().data(point.get()).build())
                .toEither()
                .mapLeft(this::mapToApplicationException);
    }
    public Either<ApplicationException, PointWalletResponse<PointUsageSummary>> debitPoint(final PointWalletRedemption pointWalletRedemption){
        return Try.ofCallable(() -> customerRepositoryPort.findById(pointWalletRedemption.getCustomerId()))
                .flatMap(customer -> {
                    if(customer.isSuccess()) {
                        final List<PointWallet> pointWallets = pointWalletRepositoryPort.findByCustomerAndBalancePointsGreaterThan(customer.get().getId(), 0)
                                .getOrElseThrow(this::mapToApplicationException);
                        final PointConcept pointConcept = pointConceptRepositoryPort.findById(pointWalletRedemption.getUsageConceptId())
                                .getOrElseThrow(this::mapToApplicationException);
                        final Try<PointUsageSummary> summary = Try.ofCallable(() -> debitWallet(pointWallets, pointConcept))
                                .map(pointsDebit -> registerUsageHeaderAndDetails(customer.get(), pointConcept, pointsDebit.get()))
                                .getOrElseThrow(this::mapToApplicationException);
                        emailSmtpPort.sendEmail(customer.get().getEmail(), "Points have been debited from your wallet", summary.get().toString());
                        return summary;
                    } else {
                        throw new ApplicationException(customer.getCause().getMessage(), HttpResponseStatus.NOT_FOUND.code());
                    }
                })
                .map(debitWallet -> PointWalletResponse.<PointUsageSummary>builder().data(debitWallet).build())
                .toEither()
                .mapLeft(this::mapToApplicationException);
    }
    private Try<List<PointUsageDetail>> debitWallet(final List<PointWallet> wallets, final PointConcept pointConcept) {
        int remainingPoints = pointConcept.getRequiredPoints();
        final List<PointUsageDetail> usageDetails = new ArrayList<>();

        for (PointWallet wallet : wallets) {
            int availablePoints = wallet.getBalancePoints();
            if (availablePoints <= 0) continue;

            int pointsToUse = Math.min(remainingPoints, availablePoints);
            wallet.setUsedPoints(wallet.getUsedPoints() + pointsToUse);
            pointWalletRepositoryPort.update(wallet.getId(), wallet);

            usageDetails.add(PointUsageDetail.builder().pointWallet(wallet).pointsUsed(pointsToUse).build());
            remainingPoints -= pointsToUse;
            if (remainingPoints <= 0) break;
        }
        if (remainingPoints > 0) {
            return Try.failure(new ApplicationException("Insufficient points for this usage", HttpResponseStatus.BAD_REQUEST.code()));
        }
        return Try.success(usageDetails);
    }
    private Try<PointUsageSummary> registerUsageHeaderAndDetails(final Customer customer, final PointConcept usageConcept, final List<PointUsageDetail> usageDetails) {
        return Try.of(() -> {
            final PointUsageHeader header = PointUsageHeader.builder()
                    .customer(customer)
                    .usageConcept(usageConcept)
                    .totalPointsUsed(usageDetails.stream().mapToInt(PointUsageDetail::getPointsUsed).sum())
                    .usageDate(LocalDateTime.now())
                    .build();
            final PointUsageHeader savedHeader = pointUsageHeaderRepositoryPort.create(header)
                    .getOrElseThrow(this::mapToApplicationException);
            usageDetails.forEach(detail -> detail.setUsageHeader(savedHeader));
            final List<PointUsageDetail> savedUsageDetail = pointUsageDetailRepositoryPort.createAll(usageDetails)
                    .getOrElseThrow(this::mapToApplicationException);
            return Try.of(() -> PointUsageSummary.builder()
                    .header(savedHeader)
                    .details(savedUsageDetail)
                    .build())
                    .getOrElseThrow(this::mapToApplicationException);
        });
    }
    private ApplicationException mapToApplicationException(final Throwable throwable) {
        if (throwable instanceof ApplicationException) {
            return (ApplicationException) throwable;
        }
        return new ApplicationException("Enexpected error points wallet", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
    }
}
