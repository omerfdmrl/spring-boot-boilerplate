package dev.nyom.backend.auth.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "JWT token response after successful refresh")
public class TokenResponse {
    @Schema(description = "Newly issued access token", example = "eyJhbGciOiJIUzI1NiIs...")
    private String accessToken;
    
    @Schema(description = "Original refresh token", example = "eyJhbGciOiJIUzI1NiIs...")
    private String refreshToken;
}
