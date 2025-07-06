package com.domainx.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
}
