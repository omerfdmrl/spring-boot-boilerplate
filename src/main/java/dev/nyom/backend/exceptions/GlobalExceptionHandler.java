package dev.nyom.backend.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<Object> handleCustomException(GlobalException ex) {
        ErrorCodes code = ex.getErrorCode();
        ErrorResponse body = this.generatErrorResponse(code, ex);

        return new ResponseEntity<>(body, code.getStatus());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialsException(Exception ex) {
        ErrorCodes code = ErrorCodes.AUTH_INVALID_CREDENTIALS;
        ErrorResponse body = this.generatErrorResponse(code, ex);

        return new ResponseEntity<>(body, code.getStatus());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> handleUsernameNotFoundException(Exception ex) {
        ErrorCodes code = ErrorCodes.AUTH_INVALID_CREDENTIALS;
        ErrorResponse body = this.generatErrorResponse(code, ex);

        return new ResponseEntity<>(body, code.getStatus());
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<Object> handleDisabledException(Exception ex) {
        ErrorCodes code = ErrorCodes.AUTH_DISABLED;
        ErrorResponse body = this.generatErrorResponse(code, ex);

        return new ResponseEntity<>(body, code.getStatus());
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<Object> handleLockedException(Exception ex) {
        ErrorCodes code = ErrorCodes.AUTH_LOCKED;
        ErrorResponse body = this.generatErrorResponse(code, ex);

        return new ResponseEntity<>(body, code.getStatus());
    }

    @ExceptionHandler(AccountExpiredException.class)
    public ResponseEntity<Object> handleAccountExpiredException(Exception ex) {
        ErrorCodes code = ErrorCodes.AUTH_EXPIRED;
        ErrorResponse body = this.generatErrorResponse(code, ex);

        return new ResponseEntity<>(body, code.getStatus());
    }

    @ExceptionHandler(CredentialsExpiredException.class)
    public ResponseEntity<Object> handleCredentialsExpiredException(Exception ex) {
        ErrorCodes code = ErrorCodes.AUTH_CREDENTIALS_EXPIRED;
        ErrorResponse body = this.generatErrorResponse(code, ex);

        return new ResponseEntity<>(body, code.getStatus());
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<Object> handleInternalAuthenticationServiceException(Exception ex) {
        ErrorCodes code = ErrorCodes.INTERNAL_SERVER_ERROR;
        ErrorResponse body = this.generatErrorResponse(code, ex);

        return new ResponseEntity<>(body, code.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        ErrorCodes code = ErrorCodes.INTERNAL_SERVER_ERROR;
        ErrorResponse body = this.generatErrorResponse(code, ex);

        return new ResponseEntity<>(body, code.getStatus());
    }

    private ErrorResponse generatErrorResponse(ErrorCodes code, Exception ex) {
        return new ErrorResponse(code.getStatus().value(), code.getCode(), ex.getMessage(), code.getStatus().getReasonPhrase());
    }
}
