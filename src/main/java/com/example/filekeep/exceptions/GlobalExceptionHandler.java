package com.example.filekeep.exceptions;

import com.example.filekeep.reponses.ApiErrorResponse;
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
    public ResponseEntity<ApiErrorResponse> handleUserAlreadyExistExceptions(UserAlreadyExistException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiErrorResponse.builder()
                        .message(ex.getMessage())
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

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleUserAlreadyExistExceptions(InvalidCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiErrorResponse.builder()
                        .message(ex.getMessage())
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
    public ResponseEntity<ApiErrorResponse> handleJwtValidationException(JwtValidationException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiErrorResponse.builder()
                        .message(ex.getMessage())
                        .build()
                );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FileNameAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleFileNameAlreadyExistsException(FileNameAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiErrorResponse.builder()
                        .message(ex.getMessage())
                        .build()
                );
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(FileDoesNotExistException.class)
    public ResponseEntity<ApiErrorResponse> handleFileDoesNotExistException(FileDoesNotExistException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiErrorResponse.builder()
                        .message(ex.getMessage())
                        .build()
                );
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ShareableLinkDoesNotExistException.class)
    public ResponseEntity<ApiErrorResponse> handleShareableLinkDoesNotExistException(ShareableLinkDoesNotExistException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiErrorResponse.builder()
                        .message(ex.getMessage())
                        .build()
                );
    }

    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    @ExceptionHandler(MaxFileStorageException.class)
    public ResponseEntity<ApiErrorResponse> handleMaxFileStorageException(MaxFileStorageException ex) {
        return ResponseEntity
                .status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(ApiErrorResponse.builder()
                        .message(ex.getMessage())
                        .build()
                );
    }
}
