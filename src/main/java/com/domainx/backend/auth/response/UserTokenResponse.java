package com.domainx.backend.auth.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserTokenResponse {
    private TokenResponse tokens;
    private UserResponse user;
}
