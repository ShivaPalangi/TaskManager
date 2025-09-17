package com.example.TaskManager.exception;

import jakarta.servlet.http.HttpServletRequest;   // برای گرفتن مسیر (URI) درخواست
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;    // ساختار استاندارد خطا
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.OffsetDateTime;     // برای زمان خطا
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFoundException(ResourceNotFoundException exception, HttpServletRequest request) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
        pd.setTitle("Resource Not Found");
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

    // @RequestBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        pd.setTitle("Validation Failed");
        pd.setProperty("path", request.getRequestURI());
        pd.setProperty("timestamp", OffsetDateTime.now());
        var errors = exception.getBindingResult().getFieldErrors().stream().map(e -> {
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("field", e.getField() != null ? e.getField() : "unknown");
            errorMap.put("message", e.getDefaultMessage() != null ? e.getDefaultMessage() : "Validation error");

            if (e.getRejectedValue() != null) {
                errorMap.put("rejectedValue", e.getRejectedValue());
            }
            return errorMap;
        }).toList();
        pd.setProperty("errors", errors);
        return pd;
    }


    // @PathVariable , @RequestParam
    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolationException(ConstraintViolationException exception, HttpServletRequest request) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        pd.setTitle("Validation Failed");
        pd.setProperty("path", request.getRequestURI());
        pd.setProperty("timestamp", OffsetDateTime.now());
        var errors = exception.getConstraintViolations().stream().map(e -> Map.of(
                "property", e.getPropertyPath().toString(),
                "message", e.getMessage(),
                "invalidValue", e.getInvalidValue()
        )).toList();
        pd.setProperty("errors", errors);
        return pd;
    }



    @ExceptionHandler(Exception.class)
    public ProblemDetail handleHttpMessageNotReadable(Exception ex, HttpServletRequest request) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "UnknownRequest");
        pd.setTitle("Bad Request");
        pd.setProperty("path", request.getRequestURI());
        pd.setProperty("timestamp", OffsetDateTime.now());
        return pd;
    }

}
