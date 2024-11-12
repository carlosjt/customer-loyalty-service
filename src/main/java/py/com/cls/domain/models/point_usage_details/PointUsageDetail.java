package py.com.cls.domain.models.point_usage_details;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import py.com.cls.domain.models.point_usage_headers.PointUsageHeader;
import py.com.cls.domain.models.point_wallets.PointWallet;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointUsageDetail {
    private Integer id;

    @NotNull(message = "Usage header cannot be null")
    private PointUsageHeader usageHeader;

    @NotNull(message = "Points used cannot be null")
    @Min(value = 1, message = "Points used must be greater than 0")
    private Integer pointsUsed;

    @NotNull(message = "Points wallet ID cannot be null")
    private PointWallet pointWallet;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
