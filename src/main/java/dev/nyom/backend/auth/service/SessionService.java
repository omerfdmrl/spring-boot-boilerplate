package dev.nyom.backend.auth.service;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import dev.nyom.backend.auth.dto.SessionDto;
import dev.nyom.backend.auth.mapper.SessionMapper;
import dev.nyom.backend.auth.model.Session;
import dev.nyom.backend.auth.model.Token;
import dev.nyom.backend.auth.model.Session.SessionDeviceType;
import dev.nyom.backend.auth.repository.SessionRepository;
import dev.nyom.backend.exceptions.ErrorCodes;
import dev.nyom.backend.exceptions.GlobalException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SessionService {
    private final SessionRepository sessionRepository;
    private final SessionMapper sessionMapper;

    public void createSession(HttpServletRequest request, Long userId, Token refreshToken) {
        InetAddress ip;
        try {
            ip = InetAddress.getByName(request.getRemoteAddr());
        } catch (Exception e) {
            throw new GlobalException(ErrorCodes.INTERNAL_SERVER_ERROR);
        }
        String userAgent = request.getHeader("User-Agent");
        Optional<Session> existingSessionOpt = sessionRepository.findByTokenUserIdAndIpAddressAndUserAgent(userId, ip, userAgent);

        Session session = existingSessionOpt.orElseGet(Session::new);
        session.setToken(refreshToken);
        session.setDeviceType(SessionDeviceType.WEB);
        session.setIpAddress(ip);
        session.setUserAgent(userAgent);
        session.setActive(true);
        this.sessionRepository.save(session);
    }

    public List<SessionDto> getSessionsForUser(Long userId) {
        return sessionRepository.findAllByTokenUserId(userId).stream()
            .map(session -> sessionMapper.toDto(session))
            .toList();
    }

    public void deleteSessionById(Long sessionId, Long userId) {
        Session session = sessionRepository.findByIdAndUserId(sessionId, userId)
            .orElseThrow(() -> new GlobalException(ErrorCodes.AUTH_SESSION_NOT_FOUND));
        sessionRepository.delete(session);
    }

    public void setSessionActive(Long sessionId, Long userId, boolean isActive) {
        Session session = sessionRepository.findByIdAndUserId(sessionId, userId)
            .orElseThrow(() -> new GlobalException(ErrorCodes.AUTH_SESSION_NOT_FOUND));
        session.setActive(isActive);
        session.setLastUsedAt(LocalDateTime.now());
        sessionRepository.save(session);
    }
}
