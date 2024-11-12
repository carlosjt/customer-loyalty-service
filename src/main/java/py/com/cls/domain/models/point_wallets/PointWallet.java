package py.com.cls.domain.models.point_wallets;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import py.com.cls.domain.models.customer.Customer;
import py.com.cls.domain.models.point_expiration.PointExpiration;
import py.com.cls.domain.models.point_rule.PointRule;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class PointWallet {
    private Integer id;

    @NotNull(message = "Customer cannot be null")
    private Customer customer;

    @NotNull(message = "Point Expiration cannot be null")
    private PointExpiration pointExpiration;

    @NotNull(message = "Point Rule cannot be null")
    private PointRule pointRule;

    @NotNull(message = "Assignment date cannot be null")
    private LocalDate assignmentDate;

    @NotNull(message = "Assigned points cannot be null")
    @Min(value = 0, message = "Assigned points must be greater than or equal to 0")
    private Integer assignedPoints;

    @NotNull(message = "Used points cannot be null")
    @Min(value = 0, message = "Used points must be greater than or equal to 0")
    private Integer usedPoints;

    private Integer balancePoints;

    @NotNull(message = "Transaction amount cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Transaction amount must be greater than 0")
    private BigDecimal transactionAmount;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
