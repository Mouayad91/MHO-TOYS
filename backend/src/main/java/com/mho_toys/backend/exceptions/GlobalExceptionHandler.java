package com.mho_toys.backend.exceptions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(err -> {
            String field = ((FieldError) err).getField();
            String message = ((FieldError) err).getDefaultMessage();
            errors.put(field, sanitizeErrorMessage(message));
        });
        
        logger.warn("Validation error occurred: {}", errors);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleResourceNotFoundException(ResourceNotFoundException e) {
        logger.warn("Resource not found: {}", e.getMessage());
        
        ApiResponse apiResponse = new ApiResponse(
            LocalDateTime.now(), 
            HttpStatus.NOT_FOUND.value(), 
            "The requested resource was not found", 
            getRequestPath()
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        logger.warn("Invalid argument: {}", e.getMessage());
        
        ApiResponse apiResponse = new ApiResponse(
            LocalDateTime.now(), 
            HttpStatus.BAD_REQUEST.value(), 
            sanitizeErrorMessage(e.getMessage()), 
            getRequestPath()
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException e) {
        logger.warn("Access denied: {}", e.getMessage());
        
        ApiResponse apiResponse = new ApiResponse(
            LocalDateTime.now(), 
            HttpStatus.FORBIDDEN.value(), 
            "Access denied", 
            getRequestPath()
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGenericException(Exception e) {
        logger.error("Unexpected error occurred", e);
        
        // Don't expose internal error details to prevent information disclosure
        ApiResponse apiResponse = new ApiResponse(
            LocalDateTime.now(), 
            HttpStatus.INTERNAL_SERVER_ERROR.value(), 
            "An unexpected error occurred. Please try again later.", 
            getRequestPath()
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException e) {
        logger.error("Runtime exception occurred: {}", e.getMessage(), e);
        
        // Sanitize error message to prevent information disclosure
        String sanitizedMessage = sanitizeErrorMessage(e.getMessage());
        
        ApiResponse apiResponse = new ApiResponse(
            LocalDateTime.now(), 
            HttpStatus.BAD_REQUEST.value(), 
            sanitizedMessage, 
            getRequestPath()
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Sanitize error messages to prevent information disclosure
     */
    private String sanitizeErrorMessage(String message) {
        if (message == null) {
            return "An error occurred";
        }
        
        // Remove potentially sensitive information
        String sanitized = message
            .replaceAll("(?i)password", "[REDACTED]")
            .replaceAll("(?i)secret", "[REDACTED]")
            .replaceAll("(?i)token", "[REDACTED]")
            .replaceAll("(?i)key", "[REDACTED]")
            .replaceAll("\\b\\d{16}\\b", "[CARD NUMBER]") // Credit card numbers
            .replaceAll("\\b\\d{3}-\\d{2}-\\d{4}\\b", "[SSN]"); // SSN pattern
        
        // Limit message length
        return sanitized.length() > 200 ? sanitized.substring(0, 200) + "..." : sanitized;
    }

    /**
     * Get the current request path safely
     */
    private String getRequestPath() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            return request.getRequestURI();
        } catch (Exception e) {
            return "/unknown";
        }
    }
}
