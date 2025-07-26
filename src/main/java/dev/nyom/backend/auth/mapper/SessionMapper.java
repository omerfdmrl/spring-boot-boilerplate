package dev.nyom.backend.auth.mapper;

import org.springframework.stereotype.Component;

import dev.nyom.backend.auth.dto.SessionDto;
import dev.nyom.backend.auth.model.Session;

@Component
public class SessionMapper {
    public SessionDto toDto(Session session) {
        SessionDto dto = new SessionDto();
        dto.setDeviceName(session.getDeviceName());
        dto.setDeviceType(session.getDeviceType());
        dto.setIpAddress(session.getIpAddress());
        dto.setUserAgent(session.getUserAgent());
        dto.setToken(TokenMapper.toDto(session.getToken()));
        dto.setActive(session.isActive());
        dto.setLastUsedAt(session.getLastUsedAt());
        return dto;
    }

    public Session toDto(SessionDto dto) {
        Session session = new Session();
        session.setDeviceName(dto.getDeviceName());
        session.setDeviceType(dto.getDeviceType());
        session.setIpAddress(dto.getIpAddress());
        session.setUserAgent(dto.getUserAgent());
        session.setToken(TokenMapper.toModel(dto.getToken()));
        session.setActive(dto.isActive());
        session.setLastUsedAt(dto.getLastUsedAt());
        return session;
    }
}
