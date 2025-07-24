package dev.nyom.backend.auth.mapper;

import org.springframework.stereotype.Component;

import dev.nyom.backend.auth.dto.TokenDto;

@Component
public class TokenMapper {
    public static TokenDto toDto(String accessToken, String refreshToken) {
        TokenDto dto = new TokenDto();
        dto.setAccessToken(accessToken);
        dto.setRefreshToken(refreshToken);
        return dto;
    }
}
