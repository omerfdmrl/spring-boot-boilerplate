package com.domainx.backend.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.domainx.backend.dto.TokenResponse;
import com.domainx.backend.model.Token;
import com.domainx.backend.model.User;
import com.domainx.backend.repository.TokenRepository;
import com.domainx.backend.repository.UserRepository;
import com.domainx.backend.security.JWTService;

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

    public TokenResponse generateJwtTokens(UserDetails userDetails) {
        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        LocalDateTime expiresAt = LocalDateTime.now().plus(REFRESH_EXPIRATION_DAYS, ChronoUnit.DAYS);
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Token token = new Token(null, refreshToken, "REFRESH_TOKEN", user, expiresAt);
        tokenRepository.save(token);

        return new TokenResponse(accessToken, refreshToken);
    }

    public String generatePasswordResetToken(UserDetails userDetails) {
        String resetPasswordToken = jwtService.generateResetPasswordToken(userDetails);

        LocalDateTime expiresAt = LocalDateTime.now().plus(REFRESH_EXPIRATION_DAYS, ChronoUnit.DAYS);
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Token token = new Token(null, resetPasswordToken, "RESET_PASSWORD", user, expiresAt);
        tokenRepository.save(token);

        return resetPasswordToken;
    }
}
