package com.domainx.backend.security;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {
    @Value("${app.security.jwt.access_expiration_minutes}")
    private long ACCESS_EXPIRATION_MINUTES;
    @Value("${app.security.jwt.refresh_expiration_days}")
    private long REFRESH_EXPIRATION_DAYS;

    private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateAccessToken(UserDetails userDetails) {
        long expirationMs = 1000L * 60 * ACCESS_EXPIRATION_MINUTES;
        return generateToken(userDetails.getUsername(), expirationMs, "ACCESS");
    }

    public String generateRefreshToken(UserDetails userDetails) {
        long expirationMs = 1000L * 60 * 60 * 24 * REFRESH_EXPIRATION_DAYS;
        return generateToken(userDetails.getUsername(), expirationMs, "REFRESH");
    }

    private String generateToken(String subject, long expirationMs, String tokenType) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .claim("type", tokenType)
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

    private boolean isTokenExpired(String token) {
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
