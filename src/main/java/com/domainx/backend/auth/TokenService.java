package com.domainx.backend.auth;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.domainx.backend.auth.response.TokenResponse;
import com.domainx.backend.security.JWTService;
import com.domainx.backend.user.models.User;
import com.domainx.backend.user.repository.UserRepository;

@Service
public class TokenService {
    @Autowired
    private JWTService jwtService;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private UserRepository userRepository;
    @Value("${app.security.jwt.refresh_expiration_days}")
    private long REFRESH_EXPIRATION_DAYS;

    public TokenResponse generateJwtTokens(String subject) {
        String accessToken = jwtService.generateAccessToken(subject);
        String refreshToken = jwtService.generateRefreshToken(subject);

        LocalDateTime expiresAt = LocalDateTime.now().plus(REFRESH_EXPIRATION_DAYS, ChronoUnit.DAYS);
        User user = userRepository.findByEmail(subject)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Token token = new Token(null, refreshToken, "REFRESH_TOKEN", user, expiresAt);
        tokenRepository.save(token);

        return new TokenResponse(accessToken, refreshToken);
    }

    public String generatePasswordResetToken(String subject) {
        String resetPasswordToken = jwtService.generateResetPasswordToken(subject);

        LocalDateTime expiresAt = LocalDateTime.now().plus(REFRESH_EXPIRATION_DAYS, ChronoUnit.DAYS);
        User user = userRepository.findByEmail(subject)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Token token = new Token(null, resetPasswordToken, "RESET_PASSWORD", user, expiresAt);
        tokenRepository.save(token);

        return resetPasswordToken;
    }
}
