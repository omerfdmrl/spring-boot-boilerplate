package dev.domainx.backend.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "User registration request payload")
public class RegisterRequest {
    @NotBlank()
    @Size(min = 4)
    @Schema(description = "Full name of the user", example = "John Doe", required = true)
    private String name;

    @NotBlank()
    @Email()
    @Schema(description = "User email", example = "john@example.com", required = true)
    private String email;

    @NotBlank()
    @Size(min = 8)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#$@!%&*?])[A-Za-z\\d#$@!%&*?]{8,}$")
    @Schema(description = "User password", example = "StrongPassword123!", required = true)
    private String password;
}
