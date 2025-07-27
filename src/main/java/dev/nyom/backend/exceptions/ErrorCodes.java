package dev.nyom.backend.exceptions;

import org.springframework.http.HttpStatus;

public enum ErrorCodes {
    AUTH_INVALID_CREDENTIALS(1000, HttpStatus.UNAUTHORIZED, "Invalid credentials"),
    AUTH_USER_NOT_FOUND(1001, HttpStatus.NOT_FOUND, "User not found"),
    AUTH_TOKEN_EXPIRED(1002, HttpStatus.UNAUTHORIZED, "Token expired"),
    AUTH_TOKEN_INVALID(1003, HttpStatus.UNAUTHORIZED, "Invalid token"),
    AUTH_EMAIL_TAKEN(1004, HttpStatus.BAD_REQUEST, "Email is already taken"),
    AUTH_RESET_TOKEN_INVALID(1005, HttpStatus.UNAUTHORIZED, "Invalid reset token"),
    AUTH_SESSION_NOT_FOUND(1006, HttpStatus.NOT_FOUND, "Session not found"),
    AUTH_INVALID_SESSION(1007, HttpStatus.UNAUTHORIZED, "Invalid session"),
    AUTH_SESSION_DISABLED(1008, HttpStatus.UNAUTHORIZED, "Session disabled"),
    AUTH_FORBIDDEN(1009, HttpStatus.FORBIDDEN, "Auth forbidden"),

    AUTH_DISABLED(1006, HttpStatus.FORBIDDEN, "Account disabled"),
    AUTH_LOCKED(1007, HttpStatus.LOCKED, "Account locked"),
    AUTH_EXPIRED(1008, HttpStatus.FORBIDDEN, "Account expired"),
    AUTH_CREDENTIALS_EXPIRED(1009, HttpStatus.UNAUTHORIZED, "Account credentials expired"),

    USER_UNAUTHORIZED(2000, HttpStatus.UNAUTHORIZED, "Unauthorized"),
    USER_NOT_FOUND(2001, HttpStatus.NOT_FOUND, "User not found"),
    USER_PROFILE_NOT_FOUND(2002, HttpStatus.NOT_FOUND, "User profile not found"),
    USER_CONNOT_FOLLOW_YOURSELF(2003, HttpStatus.BAD_REQUEST, "User cannot follow theirself"),

    SETTING_NOT_FOUND(8000, HttpStatus.NOT_FOUND, "Setting not found"),

    INTERNAL_SERVER_ERROR(9000, HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected server error"),
    VALIDATION_ERROR(9001, HttpStatus.BAD_REQUEST, "Validation Failed");

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
