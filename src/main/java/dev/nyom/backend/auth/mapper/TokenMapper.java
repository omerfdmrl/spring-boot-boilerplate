package dev.nyom.backend.auth.mapper;

import org.springframework.stereotype.Component;

import dev.nyom.backend.auth.dto.TokenDto;
import dev.nyom.backend.auth.model.Token;

@Component
public class TokenMapper {
    public static TokenDto toDto(Token token) {
        TokenDto dto = new TokenDto();
        dto.setToken(token.getToken());
        dto.setType(token.getType());
        return dto;
    }

    public static Token toModel(TokenDto dto) {
        Token token = new Token();
        token.setToken(dto.getToken());
        token.setType(dto.getType());
        return token;
    }
}
