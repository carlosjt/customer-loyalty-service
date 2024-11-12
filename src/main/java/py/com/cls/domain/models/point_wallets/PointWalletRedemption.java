package py.com.cls.domain.models.point_wallets;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PointWalletRedemption {

    @NotNull(message = "Customer ID cannot be null")
    private Integer customerId;

    @NotNull(message = "Usage concept ID is required")
    private Integer usageConceptId;
}
