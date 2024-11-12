package py.com.cls.infrastructure.out.repository.point_usage_details.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import py.com.cls.infrastructure.out.repository.point_usage_headers.entity.PointsUsageHeader;
import py.com.cls.infrastructure.out.repository.point_wallets.entity.PointsWallet;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "t_points_usage_detail")
public class PointsUsageDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "usage_header_id", nullable = false)
    private PointsUsageHeader usageHeader;

    @Column(name = "points_used", nullable = false)
    private Integer pointsUsed;

    @ManyToOne
    @JoinColumn(name = "points_wallet_id", nullable = false)
    private PointsWallet pointWallet;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

