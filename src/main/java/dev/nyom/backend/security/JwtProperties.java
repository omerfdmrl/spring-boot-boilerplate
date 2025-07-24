package dev.nyom.backend.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "app.security.jwt")
@Data
public class JwtProperties {
    private Integer accessExpirationMinutes;
    private Integer refreshExpirationDays;
    private Integer forgotPasswordExpirationMinutes;
    private String secret;
}
