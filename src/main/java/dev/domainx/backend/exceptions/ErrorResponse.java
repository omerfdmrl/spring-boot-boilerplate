package dev.domainx.backend.exceptions;

import java.time.Instant;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Error Response", description = "Global error response")
public class ErrorResponse {
    @Schema(description = "Timestamp when the error occurred", example = "2025-07-22T18:19:49.128Z")
    private final Instant timestamp = Instant.now();

    @Schema(description = "HTTP status code of the error response", example = "400")
    private final int status;

    @Schema(description = "Application-specific error code", example = "1000")
    private final int code;

    @Schema(description = "Detailed message describing the error", example = "Email is invalid")
    private final String message;

    @Schema(description = "Reason or cause of the error", example = "Email validation failed")
    private final String error;

    public ErrorResponse(int status, int code, String message, String error) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.error = error;
    }

    public Instant getTimestamp() { return timestamp; }
    public int getStatus() { return status; }
    public int getCode() { return code; }
    public String getMessage() { return message; }
    public String getError() { return error; }
}
