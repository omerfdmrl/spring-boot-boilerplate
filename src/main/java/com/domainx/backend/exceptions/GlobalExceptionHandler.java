package com.domainx.backend.exceptions;

import java.util.HashMap;
import java.util.Map;
import java.time.Instant;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<Object> handleCustomException(GlobalException ex) {
        ErrorCodes code = ex.getErrorCode();
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now());
        body.put("status", code.getStatus().value());
        body.put("error", code.getStatus().getReasonPhrase());
        body.put("message", code.getMessage());
        body.put("code", code.getCode());

        return new ResponseEntity<>(body, code.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        ErrorCodes code = ErrorCodes.INTERNAL_SERVER_ERROR;
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now());
        body.put("status", code.getStatus().value());
        body.put("error", code.getStatus().getReasonPhrase());
        body.put("message", ex.getMessage());
        body.put("code", code.getCode());

        return new ResponseEntity<>(body, code.getStatus());
    }
}
