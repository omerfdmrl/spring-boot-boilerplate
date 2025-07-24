package dev.nyom.backend.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    private final JwtProperties jwtProperties;
    private final Key key;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(String subject) {
        long expirationMs = 1000L * 60 * this.jwtProperties.getAccessExpirationMinutes();
        return generateToken(subject, expirationMs, "ACCESS");
    }

    public String generateRefreshToken(String subject) {
        long expirationMs = 1000L * 60 * 60 * 24 * this.jwtProperties.getRefreshExpirationDays();
        return generateToken(subject, expirationMs, "REFRESH");
    }

    public String generateResetPasswordToken(String subject) {
        long expirationMs = 1000L * 60 * this.jwtProperties.getForgotPasswordExpirationMinutes();
        return generateToken(subject, expirationMs, "RESET_PASSWORD");
    }

    private String generateToken(String subject, long expirationMs, String tokenType) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .claim("type", tokenType)
                .setId(UUID.randomUUID().toString())
                .signWith(key)
                .compact();
    }

    public String extractSubject(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails, String tokenType) {
        String subject = extractSubject(token);
        return subject.equals(userDetails.getUsername()) && !isTokenExpired(token) && tokenType.equals(extractTokenType(token));
    }

    public boolean isTokenExpired(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }

    public String extractTokenType(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("type", String.class);
    }
}
