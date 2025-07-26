package dev.nyom.backend.auth.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;

import dev.nyom.backend.auth.model.Token;
import dev.nyom.backend.auth.model.Token.TokenType;
import dev.nyom.backend.auth.repository.TokenRepository;
import dev.nyom.backend.exceptions.ErrorCodes;
import dev.nyom.backend.exceptions.GlobalException;
import dev.nyom.backend.security.JwtProperties;
import dev.nyom.backend.security.JwtService;
import dev.nyom.backend.user.model.User;
import dev.nyom.backend.user.repository.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TokenService {
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final JwtProperties jwtProperties;

    public String generateAccessToken(String subject) {
        String accessToken = jwtService.generateAccessToken(subject);

        return accessToken;
    }

    public Token generateRefreshToken(String subject) {
        String refreshToken = jwtService.generateRefreshToken(subject);

        LocalDateTime expiresAt = LocalDateTime.now().plus(this.jwtProperties.getForgotPasswordExpirationMinutes(), ChronoUnit.DAYS);
        User user = userRepository.findByEmail(subject)
                .orElseThrow(() -> new GlobalException(ErrorCodes.AUTH_USER_NOT_FOUND));

        Token token = new Token(null, refreshToken, TokenType.REFRESH, user, expiresAt);
        tokenRepository.save(token);

        return token;
    }

    public Token generatePasswordResetToken(String subject) {
        String resetPasswordToken = jwtService.generateResetPasswordToken(subject);

        LocalDateTime expiresAt = LocalDateTime.now().plus(this.jwtProperties.getForgotPasswordExpirationMinutes(), ChronoUnit.DAYS);
        User user = userRepository.findByEmail(subject)
                .orElseThrow(() -> new GlobalException(ErrorCodes.AUTH_USER_NOT_FOUND));

        Token token = new Token(null, resetPasswordToken, TokenType.PASSWORD_RESET, user, expiresAt);
        tokenRepository.save(token);

        return token;
    }
}
