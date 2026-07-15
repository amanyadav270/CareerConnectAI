package com.placement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class global_exception_handler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handle_bad_request(IllegalArgumentException ex) {
        return build_error_response(HttpStatus.BAD_REQUEST, "BAD_REQUEST", ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handle_conflict(IllegalStateException ex) {
        return build_error_response(HttpStatus.CONFLICT, "CONFLICT_ERROR", ex.getMessage());
    }

    private ResponseEntity<Map<String, Object>> build_error_response(HttpStatus status, String code, String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", status.value());
        error.put("code", code);
        error.put("message", message);
        return new ResponseEntity<>(error, status);
    }
}