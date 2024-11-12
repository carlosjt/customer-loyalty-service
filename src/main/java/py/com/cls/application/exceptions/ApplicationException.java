package py.com.cls.application.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationException extends RuntimeException {
    private final int httpStatusCode;
    public ApplicationException(final String message, final int httpStatusCode) {
        super(message);
        this.httpStatusCode = httpStatusCode;
    }
}

