package py.com.cls.infrastructure.in.adapters.errors;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ErrorResponse {
    private final int errorCode;
    private final String message;
}
