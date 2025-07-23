package dev.domainx.backend.exceptions;

public class GlobalException extends RuntimeException {
    private final ErrorCodes errorCode;

    public GlobalException(ErrorCodes errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCodes getErrorCode() {
        return errorCode;
    }
}
