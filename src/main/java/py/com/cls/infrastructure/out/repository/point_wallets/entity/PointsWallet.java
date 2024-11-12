package py.com.cls.infrastructure.out.repository.point_wallets.entity;

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
import py.com.cls.infrastructure.out.repository.customers.entity.Customers;
import py.com.cls.infrastructure.out.repository.point_expirations.entity.PointsExpiration;
import py.com.cls.infrastructure.out.repository.point_rules.entity.PointsRule;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "t_points_wallet")
public class PointsWallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "assignment_date", nullable = false)
    private LocalDate assignmentDate;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customers customer;

    @ManyToOne
    @JoinColumn(name = "expiration_id")
    private PointsExpiration pointExpiration;

    @ManyToOne
    @JoinColumn(name = "rule_id", nullable = false)
    private PointsRule pointRule;

    @Column(name = "assigned_points", nullable = false)
    private Integer assignedPoints;

    @Column(name = "used_points", nullable = false)
    private Integer usedPoints = 0;

    @Column(name = "balance_points", insertable = false, updatable = false)
    private Integer balancePoints;

    @Column(name = "transaction_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal transactionAmount;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
