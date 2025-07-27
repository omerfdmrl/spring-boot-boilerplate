package dev.nyom.backend.auth.model;

import java.net.InetAddress;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sessions")
@Schema(description = "Represents a users seessions and devices.")
public class Session {
    public enum SessionDeviceType {
        WEB("WEB"),
        MOBILE("MOBILE"),
        DESKTOP("DESKTOP");

        private final String value;

        SessionDeviceType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static SessionDeviceType fromValue(String value) {
            for (SessionDeviceType type : values()) {
                if (type.value.equalsIgnoreCase(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown session type: " + value);
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the session", example = "1")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "device_type")
    @Schema(description = "Device type", example = "web")
    private SessionDeviceType deviceType;

    @Column(name = "device_name")
    @Schema(description = "Device name", example = "Joo's Iphone")
    private String deviceName;

    @Column(name = "ip_address", columnDefinition = "inet")
    @Schema(description = "Device's IP address", example = "192.168.1.1")
    private InetAddress ipAddress;

    @Column(name = "user_agent")
    @Schema(description = "Devices user-agent informations")
    private String userAgent;

    @Column(name = "is_active")
    @Schema(description = "Is the device session active?", example = "true")
    private boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "token_id", nullable = false)
    @Schema(description = "The token this session is associated with")
    private Token token;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    @Schema(description = "Date and time when the session was created", example = "2025-07-22T10:00:00")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "last_used_at", nullable = false)
    @Schema(description = "Date and time when the session was last used", example = "2025-07-22T12:30:00")
    private LocalDateTime lastUsedAt;
}
