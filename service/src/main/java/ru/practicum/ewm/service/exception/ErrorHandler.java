package ru.practicum.ewm.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

import static org.hibernate.type.LocalTimeType.FORMATTER;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse objectNotFoundExceptionResponse(final ResourceNotFoundException e) {
        return new ErrorResponse(e.getMessage(),
                "NOT_FOUND", "Resource not found", LocalDateTime.now().format(FORMATTER));
    }
}
