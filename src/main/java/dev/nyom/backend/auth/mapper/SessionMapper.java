package dev.nyom.backend.auth.mapper;

import org.springframework.stereotype.Component;

import dev.nyom.backend.auth.dto.SessionDto;
import dev.nyom.backend.auth.model.Session;

@Component
public class SessionMapper {
    public SessionDto toDto(Session entity) {
        SessionDto dto = new SessionDto();
        dto.setDeviceName(entity.getDeviceName());
        dto.setDeviceType(entity.getDeviceType());
        dto.setIpAddress(entity.getIpAddress());
        dto.setUserAgent(entity.getUserAgent());
        dto.setToken(TokenMapper.toDto(entity.getToken()));
        dto.setActive(entity.isActive());
        dto.setLastUsedAt(entity.getLastUsedAt());
        return dto;
    }

    public Session toDto(SessionDto dto) {
        Session entity = new Session();
        entity.setDeviceName(dto.getDeviceName());
        entity.setDeviceType(dto.getDeviceType());
        entity.setIpAddress(dto.getIpAddress());
        entity.setUserAgent(dto.getUserAgent());
        entity.setToken(TokenMapper.toModel(dto.getToken()));
        entity.setActive(dto.isActive());
        entity.setLastUsedAt(dto.getLastUsedAt());
        return entity;
    }
}
