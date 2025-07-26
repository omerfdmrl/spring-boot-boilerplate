package dev.nyom.backend.auth.dto;

import dev.nyom.backend.auth.model.Token.TokenType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Token public schema.")
public class TokenDto {
    @Schema(description = "Token", example = "eyJhbGciOiJIUzI1NiIs...")
    private String token;
    
    @Schema(description = "Type of the token", example = "REFRESH")
    private TokenType type;
}
