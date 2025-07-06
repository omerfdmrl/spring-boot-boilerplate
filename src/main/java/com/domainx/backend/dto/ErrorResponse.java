package com.domainx.backend.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErrorResponse {
    private String type = "error";
    private String message;
    private Map<String, String> errors;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public ErrorResponse(Map<String, String> errors) {
        this.message = "Validation Error";
        this.errors = errors;
    }
}
