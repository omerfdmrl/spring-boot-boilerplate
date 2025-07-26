package dev.nyom.backend.auth.response;

import dev.nyom.backend.user.dto.UserDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
@Schema(description = "JWT token and user informations response")
public class UserTokenResponse {
    private TokenResponse tokens;
    private UserDto user;
}
