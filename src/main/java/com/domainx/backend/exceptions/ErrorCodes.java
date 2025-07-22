package com.domainx.backend.exceptions;

import org.springframework.http.HttpStatus;

public enum ErrorCodes {
    AUTH_INVALID_CREDENTIALS(1000, HttpStatus.UNAUTHORIZED, "Invalid credentials"),
    AUTH_USER_NOT_FOUND(1001, HttpStatus.NOT_FOUND, "User not found"),
    AUTH_TOKEN_EXPIRED(1002, HttpStatus.UNAUTHORIZED, "Token expired"),
    AUTH_TOKEN_INVALID(1003, HttpStatus.UNAUTHORIZED, "Invalid token"),
    AUTH_EMAIL_TAKEN(1004, HttpStatus.BAD_REQUEST, "Email is already taken"),
    AUTH_RESET_TOKEN_INVALID(1005, HttpStatus.UNAUTHORIZED, "Invalid reset token"),

    USER_UNAUTHORIZED(2000, HttpStatus.UNAUTHORIZED, "Unauthorized"),

    INTERNAL_SERVER_ERROR(4000, HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected server error");

    private final int code;
    private final HttpStatus status;
    private final String message;

    ErrorCodes(int code, HttpStatus status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
