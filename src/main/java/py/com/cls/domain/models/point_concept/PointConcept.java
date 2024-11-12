package py.com.cls.domain.models.point_concept;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PointConcept {
    private Integer id;

    @NotBlank(message = "Concept description cannot be blank")
    @Size(max = 100, message = "Concept description must be at most 100 characters")
    private String conceptDescription;

    @NotNull(message = "Required points cannot be null")
    @Min(value = 1, message = "Required points must be greater than 0")
    private Integer requiredPoints;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
