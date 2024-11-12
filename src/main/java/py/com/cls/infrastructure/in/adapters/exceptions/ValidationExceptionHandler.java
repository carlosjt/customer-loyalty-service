package py.com.cls.infrastructure.in.adapters.exceptions;

import io.netty.handler.codec.http.HttpResponseStatus;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import py.com.cls.infrastructure.in.adapters.exceptions.errors.ErrorResponse;

import java.util.stream.Collectors;

@Provider
@Slf4j
public class ValidationExceptionHandler implements ExceptionMapper<ConstraintViolationException> {
    @Override
    public Response toResponse(final ConstraintViolationException exception) {
        final String errors = exception.getConstraintViolations().stream()
                .map(violation -> String.format(
                        "{\"objectName\": \"%s\", \"attributeName\": \"%s\", \"value\": \"%s\", \"message\": \"%s\"}",
                        violation.getRootBeanClass().getSimpleName(),
                        violation.getPropertyPath().toString(),
                        violation.getInvalidValue(),
                        violation.getMessage()))
                .collect(Collectors.joining(", ", "[", "]"));
        log.error("errorId[{}]", errors);
        return Response.status(HttpResponseStatus.NOT_FOUND.code())
                .entity(ErrorResponse.builder()
                        .errorCode(HttpResponseStatus.NOT_FOUND.code())
                        .message(errors).build())
                .build();
    }
}
