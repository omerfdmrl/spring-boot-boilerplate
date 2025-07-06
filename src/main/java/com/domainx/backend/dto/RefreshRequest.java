package com.domainx.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshRequest {
    @NotBlank(message = "Refresh token is mandatory")
    private String refreshToken;
}
