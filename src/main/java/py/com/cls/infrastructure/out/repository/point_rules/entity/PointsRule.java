package py.com.cls.infrastructure.out.repository.point_rules.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "t_points_rules")
public class PointsRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "lower_limit", nullable = false, precision = 12, scale = 2)
    @DecimalMin(value = "0.00", inclusive = true, message = "Lower limit must be greater than or equal to 0")
    private BigDecimal lowerLimit;

    @Column(name = "upper_limit", nullable = false, precision = 12, scale = 2)
    @DecimalMin(value = "0.00", inclusive = true, message = "Upper limit must be greater than or equal to 0")
    private BigDecimal upperLimit;

    @Column(name = "equivalence_amount", nullable = false, precision = 12, scale = 2)
    @NotNull(message = "Equivalence amount may not be null")
    @Positive(message = "Equivalence amount must be greater than 0")
    private BigDecimal equivalenceAmount;

    @Column(name = "points_awarded", nullable = false)
    @NotNull(message = "Points awarded may not be null")
    @Min(value = 1, message = "Points awarded must be greater than 0")
    private Integer pointsAwarded;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

