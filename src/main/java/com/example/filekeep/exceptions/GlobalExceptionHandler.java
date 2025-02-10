package com.example.filekeep.exceptions;

import com.example.filekeep.enums.Status;
import com.example.filekeep.reponses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Exception handler for UserAlreadyExistException.
     *
     * @param ex The UserAlreadyExistException that occurred.
     * @return ResponseEntity containing ApiResponse with a conflict status and an error message.
     */
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ApiResponse<String>> handleUserAlreadyExistExceptions(UserAlreadyExistException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.<String>builder()
                        .message(ex.getMessage())
                        .status(Status.ERROR)
                        .build()
                );
    }

    /**
     * Exception handler for MethodArgumentNotValidException.
     *
     * @param ex The MethodArgumentNotValidException that occurred.
     * @return ResponseEntity containing error messages for validation errors.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<ApiResponse<String>> handleValidationExceptions(PasswordMismatchException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.<String>builder()
                        .message(ex.getMessage())
                        .status(Status.ERROR)
                        .build()
                );
    }


    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiResponse<String>> handleUserAlreadyExistExceptions(InvalidCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.<String>builder()
                        .message(ex.getMessage())
                        .status(Status.ERROR)
                        .build()
                );
    }

    /**
     * Exception handler for JwtValidationException.
     *
     * @param ex The JwtValidationException that occurred.
     * @return ResponseEntity containing an error message with unauthorized status.
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(JwtValidationException.class)
    public ResponseEntity<ApiResponse<String>> handleJwtValidationException(JwtValidationException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.<String>builder()
                        .data(null)
                        .message(ex.getMessage())
                        .status(Status.ERROR)
                        .path("/api/v1/auth/logged_in")
                        .build()
                );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FileNameAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<String>> handleFileNameAlreadyExistsException(FileNameAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.<String>builder()
                        .data(null)
                        .message(ex.getMessage())
                        .status(Status.ERROR)
                        .path("/api/v1/upload")
                        .build()
                );
    }
}
