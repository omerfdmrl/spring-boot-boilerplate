package dev.nyom.backend.auth.dto;

import java.net.InetAddress;
import java.time.LocalDateTime;

import dev.nyom.backend.auth.model.Session.SessionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Session public schema.")
public class SessionDto {
    @Schema(description = "Device type", example = "web")
    private SessionType deviceType;
    
    @Schema(description = "Device name", example = "Joo's Iphone")
    private String deviceName;

    @Schema(description = "Device's IP address", example = "192.168.1.1")
    private InetAddress ipAddress;

    @Schema(description = "Devices user-agent informations")
    private String userAgent;

    @Schema(description = "Is the device session active?", example = "true")
    private boolean isActive = true;

    @Schema(description = "The token this session is associated with")
    private TokenDto token;

    @Schema(description = "Date and time when the session was last used", example = "2025-07-22T12:30:00")
    private LocalDateTime lastUsedAt;
}
