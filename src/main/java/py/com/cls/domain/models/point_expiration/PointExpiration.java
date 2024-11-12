package py.com.cls.domain.models.point_expiration;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class PointExpiration {
    private Integer id;

    @NotNull(message = "Start date cannot be null")
    private LocalDate startDate;

    @NotNull(message = "End date cannot be null")
    private LocalDate endDate;

    private Integer durationDays;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
