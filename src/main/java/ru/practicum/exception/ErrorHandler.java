// exception/controller/ErrorHandler.java (Improved)
package ru.practicum.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException; // Import
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.FieldError; // Import
import java.util.Map;
import java.util.HashMap; // Import

@RestControllerAdvice
@Slf4j
@Getter
public class ErrorHandler {

    @ExceptionHandler(ValidateException.class)  // Use the specific exception
    public ResponseEntity<ErrorResponse> handleValidateException(final ValidateException ex) {
        log.error("Validation error: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("validation_error", ex.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class) // Use the specific exception
    public ResponseEntity<ErrorResponse> handleNotFoundException(final NotFoundException ex) {
        log.error("Not found error: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("not_found", ex.getMessage()));
    }

    @ExceptionHandler(DuplicateEmailException.class) // Handle the *specific* exception
    public ResponseEntity<ErrorResponse> handleDuplicateEmailException(final DuplicateEmailException ex) {
        log.error("Conflict error: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("duplicate_email", ex.getMessage()));
    }

    // Handle MethodArgumentNotValidException (for Bean Validation)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        log.error("Validation error: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    // Catch-all for other exceptions (optional, but good practice)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleOtherExceptions(final Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex); // Log the full stack trace
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("internal_server_error", "An unexpected error occurred."));
    }
}