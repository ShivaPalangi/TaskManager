package com.example.TaskManager.exception;

import jakarta.servlet.http.HttpServletRequest;   // برای گرفتن مسیر (URI) درخواست
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;    // ساختار استاندارد خطا
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.OffsetDateTime;     // برای زمان خطا

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFoundException(BadRequestException exception, HttpServletRequest request) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
        pd.setTitle("Bad Request");
        pd.setProperty("path", request.getRequestURI());
        pd.setProperty("timestamp", OffsetDateTime.now());
        return pd;
    }


    @ExceptionHandler(BadRequestException.class)
    public ProblemDetail handleBadRequestException(BadRequestException exception, HttpServletRequest request) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        pd.setTitle("Bad Request");
        pd.setProperty("path", request.getRequestURI());
        pd.setProperty("timestamp", OffsetDateTime.now());
        return pd;
    }
}
