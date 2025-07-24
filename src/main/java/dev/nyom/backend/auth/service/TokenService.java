package dev.nyom.backend.auth.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;

import dev.nyom.backend.auth.dto.TokenDto;
import dev.nyom.backend.auth.mapper.TokenMapper;
import dev.nyom.backend.auth.model.Token;
import dev.nyom.backend.auth.repository.TokenRepository;
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

    public TokenDto generateJwtTokens(String subject) {
        String accessToken = jwtService.generateAccessToken(subject);
        String refreshToken = jwtService.generateRefreshToken(subject);

        LocalDateTime expiresAt = LocalDateTime.now().plus(this.jwtProperties.getRefreshExpirationDays(), ChronoUnit.DAYS);
        User user = userRepository.findByEmail(subject)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Token token = new Token(null, refreshToken, "REFRESH_TOKEN", user, expiresAt);
        tokenRepository.save(token);

        return TokenMapper.toDto(accessToken, refreshToken);
    }

    public String generatePasswordResetToken(String subject) {
        String resetPasswordToken = jwtService.generateResetPasswordToken(subject);

        LocalDateTime expiresAt = LocalDateTime.now().plus(this.jwtProperties.getForgotPasswordExpirationMinutes(), ChronoUnit.DAYS);
        User user = userRepository.findByEmail(subject)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Token token = new Token(null, resetPasswordToken, "RESET_PASSWORD", user, expiresAt);
        tokenRepository.save(token);

        return resetPasswordToken;
    }
}
