package dev.nyom.backend.auth.repository;

import java.net.InetAddress;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dev.nyom.backend.auth.model.Session;
import jakarta.transaction.Transactional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findByTokenId(Long id);

    Optional<Session> findByTokenUserIdAndIpAddressAndUserAgent(Long userId, InetAddress ip, String userAgent);

    @Modifying
    @Transactional
    @Query(value = """
        UPDATE sessions
        SET last_used_at = now()
        WHERE token_id = (
            SELECT id FROM tokens WHERE token = :refreshToken
        )
        """, nativeQuery = true)
    int updateLastUsedAtByRefreshToken(String refreshToken);

    @Query(value = """
        SELECT s.*
        FROM sessions s
        JOIN tokens t ON t.id = s.token_id
        WHERE t.token = :refreshToken
        """, nativeQuery = true)
    Optional<Session> findSessionByRefreshToken(String refreshToken);

    List<Session> findAllByTokenUserId(Long userId);

    @Query("""
        SELECT s FROM Session s
        WHERE s.id = :sessionId AND s.token.user.id = :userId
    """)
    Optional<Session> findByIdAndUserId(@Param("sessionId") Long sessionId, @Param("userId") Long userId);
}
