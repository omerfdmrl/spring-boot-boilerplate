package dev.nyom.backend.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Request schema for login request.")
public class UserDto {
    @NotBlank()
    @Size(min = 4)
    @Schema(description = "Full name of the user", example = "John Doe", required = true)
    private String name;

    @NotBlank()
    @Size(min = 4)
    @Schema(description = "Username of the User", example = "john.doe", required = true)
    private String username;

    @NotBlank()
    @Email()
    @Schema(description = "Email address of the user", example = "john@example.com", required = true)
    private String email;
}
