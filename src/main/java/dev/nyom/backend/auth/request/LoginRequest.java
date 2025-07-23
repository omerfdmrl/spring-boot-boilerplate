package dev.nyom.backend.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Request schema for login request.")
public class LoginRequest {
    @NotBlank()
    @Email()
    @Schema(description = "Email address of the user", example = "john@example.com")
    private String email;

    @NotBlank()
    @Size(min = 8)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#$@!%&*?])[A-Za-z\\d#$@!%&*?]{8,}$")
    @Schema(description = "Encoded password of the user", example = "$2a$10$...")
    private String password;
}
