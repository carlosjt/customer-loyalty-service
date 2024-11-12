package py.com.cls.infrastructure.out.repository.point_wallets;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import py.com.cls.infrastructure.out.repository.point_wallets.entity.PointsWallet;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PointsWalletRepository extends CrudRepository<PointsWallet, Integer> {
    List<PointsWallet> findByCustomer_IdAndBalancePointsGreaterThanOrderByCreatedAtAsc(final Integer id, final Integer balanceThreshold);
    @Query("SELECT pw FROM PointsWallet pw WHERE pw.balancePoints > 0 AND pw.pointExpiration.id = :currentDateId")
    List<PointsWallet> findExpiredPointsWallets(@Param("currentDateId") final Integer currentDateId);
}
