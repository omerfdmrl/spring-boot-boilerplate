package dev.domainx.backend.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Request body for refreshing JWT access token")
public class RefreshRequest {
    @NotBlank()
    @Schema(description = "Valid JWT refresh token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6...")
    private String refreshToken;
}
