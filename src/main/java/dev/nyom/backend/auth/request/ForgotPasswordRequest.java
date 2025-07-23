package dev.nyom.backend.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Request payload to trigger a password reset email")
public class ForgotPasswordRequest {
    @Email
    @NotBlank()
    @Schema(description = "Email address of the user", example = "john@example.com")
    private String email;
}
