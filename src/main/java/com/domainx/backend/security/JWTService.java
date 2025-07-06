package com.domainx.backend.security;

import java.security.Key;
import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {
    private static final long ACCESS_EXPIRATION_MS = 1000 * 60 * 15; // 15 minutes
    private static final long REFRESH_EXPIRATION_MS = 1000L * 60 * 60 * 24 * 7; // 7 days

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(userDetails.getUsername(), ACCESS_EXPIRATION_MS);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(userDetails.getUsername(), REFRESH_EXPIRATION_MS);
    }

    private String generateToken(String subject, long expirationMs) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
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

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String subject = extractSubject(token);
        return subject.equals(userDetails.getUsername()) && !isTokenExpired(token);
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
}
