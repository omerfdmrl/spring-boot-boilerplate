package dev.nyom.backend.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "JWT token response after successful refresh")
public class TokenDto {
    @Schema(description = "Newly issued access token", example = "eyJhbGciOiJIUzI1NiIs...")
    private String accessToken;
    
    @Schema(description = "Original refresh token", example = "eyJhbGciOiJIUzI1NiIs...")
    private String refreshToken;
}
