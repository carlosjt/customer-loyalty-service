package py.com.cls.domain.models.point_usage_headers;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import py.com.cls.domain.models.customer.Customer;
import py.com.cls.domain.models.point_concept.PointConcept;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointUsageHeader {
    private Integer id;

    @NotNull(message = "Customer cannot be null")
    private Customer customer;

    @NotNull(message = "Total points used cannot be null")
    @Min(value = 1, message = "Total points used must be greater than 0")
    private Integer totalPointsUsed;

    @NotNull(message = "Usage date cannot be null")
    private LocalDateTime usageDate;

    @NotNull(message = "Usage concept cannot be null")
    private PointConcept usageConcept;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
