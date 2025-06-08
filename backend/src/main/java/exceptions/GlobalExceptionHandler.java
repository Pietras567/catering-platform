package exceptions;

import dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        log.warn("Resource not found: {}", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), "Resource Not Found", HttpStatus.NOT_FOUND.value(), request.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex, HttpServletRequest request) {
        log.warn("Validation error: {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse(ex.getMessage(), "Validation Error", HttpStatus.BAD_REQUEST.value(), request.getRequestURI());

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException ex, HttpServletRequest request) {
        log.warn("User already exists: {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse(ex.getMessage(), "Conflict", HttpStatus.CONFLICT.value(), request.getRequestURI());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler({InvalidCredentialsException.class, BadCredentialsException.class})
    public ResponseEntity<ErrorResponse> handleAuthenticationException(Exception ex, HttpServletRequest request) {
        log.warn("Authentication failed: {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse("Incorrect login information", "Authentication Failed", HttpStatus.UNAUTHORIZED.value(), request.getRequestURI());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.warn("Validation failed: {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ErrorResponse error = new ErrorResponse("The input data is incorrect: " + errors, "Validation Failed", HttpStatus.BAD_REQUEST.value(), request.getRequestURI());

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex, HttpServletRequest request) {
        log.error("Database constraint violation: {}", ex.getMessage());

        String message = "Violation of database restrictions";
        if (ex.getMessage().contains("email")) {
            message = "Email is already in use";
        } else if (ex.getMessage().contains("login") || ex.getMessage().contains("username")) {
            message = "The username is already taken";
        }

        ErrorResponse error = new ErrorResponse(message, "Database Constraint Violation", HttpStatus.CONFLICT.value(), request.getRequestURI());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
        log.error("Unexpected error occurred: ", ex);

        ErrorResponse error = new ErrorResponse("An unexpected error occurred", "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR.value(), request.getRequestURI());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex, HttpServletRequest request) {
        log.error("Runtime error occurred: ", ex);

        ErrorResponse error = new ErrorResponse("An error occurred during execution: " + ex.getMessage(), "Runtime Error", HttpStatus.INTERNAL_SERVER_ERROR.value(), request.getRequestURI());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleEventRequestAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        log.warn("Access denied to event request: {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse(ex.getMessage(), "Access Denied", HttpStatus.FORBIDDEN.value(), request.getRequestURI());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }
}
