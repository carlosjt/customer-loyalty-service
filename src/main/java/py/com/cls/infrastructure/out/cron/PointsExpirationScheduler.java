package py.com.cls.infrastructure.out.cron;

import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import py.com.cls.infrastructure.out.repository.point_expirations.PointsExpirationRepository;
import py.com.cls.infrastructure.out.repository.point_expirations.entity.PointsExpiration;
import py.com.cls.infrastructure.out.repository.point_wallets.PointsWalletRepository;
import py.com.cls.infrastructure.out.repository.point_wallets.entity.PointsWallet;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@ApplicationScoped
public class PointsExpirationScheduler {
    @Inject
    PointsWalletRepository pointsWalletRepository;
    @Inject
    PointsExpirationRepository pointsExpirationRepository;
    /**
     * Scheduled method to update expired point pools
     * Runs every {expiration.job.interval} (or adjustable as needed).
     */
    @Scheduled(every = "{expiration.job.interval}")
    void updateExpiredPointsWallets() {
        log.info("Executing the expired points update process...");
        final PointsExpiration pointsExpiration = pointsExpirationRepository.findByDateWithinRange(LocalDate.now());
        if(pointsExpiration != null) {
            log.info(String.format("Executing the expired points update process from %s to %s...", pointsExpiration.getStartDate(), pointsExpiration.getEndDate()));
            final List<PointsWallet> expiredWallets = pointsWalletRepository.findExpiredPointsWallets(pointsExpiration.getId());
            for (PointsWallet wallet : expiredWallets) {
                final Integer points = wallet.getAssignedPoints();
                wallet.setUsedPoints(points);
                wallet.setBalancePoints(0);
                pointsWalletRepository.save(wallet);
            }
            log.info("Expired points update process completed.");
        }
    }
}
