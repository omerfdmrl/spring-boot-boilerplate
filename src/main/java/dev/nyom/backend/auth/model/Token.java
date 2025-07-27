package dev.nyom.backend.auth.model;

import java.time.LocalDateTime;

import dev.nyom.backend.user.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tokens")
@Schema(description = "Represents a JWT or reset token with expiration and type.")
public class Token {
    public enum TokenType {
        REFRESH("REFRESH"),
        ACCESS("ACCESS"),
        PASSWORD_RESET("PASSWORD_RESET");

        private final String value;

        TokenType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static TokenType fromValue(String value) {
            for (TokenType type : values()) {
                if (type.value.equalsIgnoreCase(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown session type: " + value);
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the token", example = "1")
    private Long id;

    @Column(nullable = false, unique = true)
    @Schema(description = "The actual token string", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Type of the token (e.g., ACCESS_TOKEN, REFRESH_TOKEN, RESET_PASSWORD)", example = "ACCESS_TOKEN")
    private TokenType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(description = "The user this token is associated with")
    private User user;

    @Column(nullable = false)
    @Schema(description = "Date and time when the token will expire", example = "2025-07-31T12:00:00")
    private LocalDateTime expiresAt;
}
