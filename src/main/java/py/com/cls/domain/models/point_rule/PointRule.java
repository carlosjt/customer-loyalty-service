package py.com.cls.domain.models.point_rule;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class PointRule {
    private Integer id;

    @DecimalMin(value = "0.00", inclusive = true, message = "Lower limit must be greater than or equal to 0")
    private BigDecimal lowerLimit;

    @DecimalMin(value = "0.00", inclusive = true, message = "Upper limit must be greater than or equal to 0")
    private BigDecimal upperLimit;

    @NotNull(message = "Equivalence amount may not be null")
    @Positive(message = "Equivalence amount must be greater than 0")
    private BigDecimal equivalenceAmount;

    @NotNull(message = "Points awarded may not be null")
    @Min(value = 1, message = "Points awarded must be greater than 0")
    private Integer pointsAwarded;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
