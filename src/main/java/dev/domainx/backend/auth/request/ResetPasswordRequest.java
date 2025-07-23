package dev.domainx.backend.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Payload to reset user password using a reset token")
public class ResetPasswordRequest {
    @NotBlank()
    @Size(min = 8)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#$@!%&*?])[A-Za-z\\d#$@!%&*?]{8,}$")
    @Schema(description = "New password for the user", required = true, example = "StrongNewPassword123!")
    private String password;

    @NotBlank()
    @Schema(description = "JWT reset token received via email", required = true, example = "eyJhbGciOiJIUzI1NiIsInR5...")
    private String token;
}
