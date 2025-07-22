package com.domainx.backend.exceptions;

import java.time.Instant;

public class ErrorResponse {
    private final Instant timestamp = Instant.now();
    private final int status;
    private final int code;
    private final String message;

    public ErrorResponse(int status, int code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    // Getters
    public Instant getTimestamp() { return timestamp; }
    public int getStatus() { return status; }
    public int getCode() { return code; }
    public String getMessage() { return message; }
}
