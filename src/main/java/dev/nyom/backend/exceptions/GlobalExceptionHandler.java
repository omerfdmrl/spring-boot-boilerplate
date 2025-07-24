package dev.nyom.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();

        String message = fieldError != null
            ? fieldError.getDefaultMessage()
            : "Validation error";

        ErrorResponse response = new ErrorResponse(
            ErrorCodes.VALIDATION_ERROR.getStatus().value(),
            ErrorCodes.VALIDATION_ERROR.getCode(),
            message,
            ErrorCodes.VALIDATION_ERROR.getMessage()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(GlobalException ex) {
        ErrorCodes code = ex.getErrorCode();
        ErrorResponse body = this.generatErrorResponse(code, ex);

        return new ResponseEntity<ErrorResponse>(body, code.getStatus());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(Exception ex) {
        ErrorCodes code = ErrorCodes.AUTH_INVALID_CREDENTIALS;
        ErrorResponse body = this.generatErrorResponse(code, ex);

        return new ResponseEntity<ErrorResponse>(body, code.getStatus());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(Exception ex) {
        ErrorCodes code = ErrorCodes.AUTH_INVALID_CREDENTIALS;
        ErrorResponse body = this.generatErrorResponse(code, ex);

        return new ResponseEntity<ErrorResponse>(body, code.getStatus());
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ErrorResponse> handleDisabledException(Exception ex) {
        ErrorCodes code = ErrorCodes.AUTH_DISABLED;
        ErrorResponse body = this.generatErrorResponse(code, ex);

        return new ResponseEntity<ErrorResponse>(body, code.getStatus());
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ErrorResponse> handleLockedException(Exception ex) {
        ErrorCodes code = ErrorCodes.AUTH_LOCKED;
        ErrorResponse body = this.generatErrorResponse(code, ex);

        return new ResponseEntity<ErrorResponse>(body, code.getStatus());
    }

    @ExceptionHandler(AccountExpiredException.class)
    public ResponseEntity<ErrorResponse> handleAccountExpiredException(Exception ex) {
        ErrorCodes code = ErrorCodes.AUTH_EXPIRED;
        ErrorResponse body = this.generatErrorResponse(code, ex);

        return new ResponseEntity<ErrorResponse>(body, code.getStatus());
    }

    @ExceptionHandler(CredentialsExpiredException.class)
    public ResponseEntity<ErrorResponse> handleCredentialsExpiredException(Exception ex) {
        ErrorCodes code = ErrorCodes.AUTH_CREDENTIALS_EXPIRED;
        ErrorResponse body = this.generatErrorResponse(code, ex);

        return new ResponseEntity<ErrorResponse>(body, code.getStatus());
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<ErrorResponse> handleInternalAuthenticationServiceException(Exception ex) {
        ErrorCodes code = ErrorCodes.INTERNAL_SERVER_ERROR;
        ErrorResponse body = this.generatErrorResponse(code, ex);

        return new ResponseEntity<ErrorResponse>(body, code.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorCodes code = ErrorCodes.INTERNAL_SERVER_ERROR;
        ErrorResponse body = this.generatErrorResponse(code, ex);

        return new ResponseEntity<ErrorResponse>(body, code.getStatus());
    }

    private ErrorResponse generatErrorResponse(ErrorCodes code, Exception ex) {
        return new ErrorResponse(code.getStatus().value(), code.getCode(), ex.getMessage(), code.getStatus().getReasonPhrase());
    }
}
