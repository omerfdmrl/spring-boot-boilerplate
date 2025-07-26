package dev.nyom.backend.user;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.nyom.backend.auth.dto.SessionDto;
import dev.nyom.backend.auth.service.SessionService;
import dev.nyom.backend.user.dto.UserDto;
import dev.nyom.backend.user.impl.CustomUserDetails;
import dev.nyom.backend.user.mapper.UserMapper;
import dev.nyom.backend.user.model.User;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserMapper userMapper;
    private final SessionService sessionService;

    @GetMapping("/me")
    public ResponseEntity<UserDto> me(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        UserDto userDto = this.userMapper.toDto(user);
        return ResponseEntity.ok(userDto);
    }

    @Operation(summary = "List all sessions for current user")
    @GetMapping("/session")
    public ResponseEntity<List<SessionDto>> listSessions(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<SessionDto> sessions = sessionService.getSessionsForUser(userDetails.getUser().getId());
        return ResponseEntity.ok(sessions);
    }

    @Operation(summary = "Delete a session by ID")
    @DeleteMapping("/session/{id}/delete")
    public ResponseEntity<String> deleteSession(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        sessionService.deleteSessionById(id, userDetails.getUser().getId());
        return ResponseEntity.ok("OK");
    }

    @Operation(summary = "Disable a session by ID")
    @PatchMapping("/session/{id}/disable")
    public ResponseEntity<String> disableSession(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        sessionService.setSessionActive(id, userDetails.getUser().getId(), false);
        return ResponseEntity.ok("OK");
    }

    @Operation(summary = "Enable a session by ID")
    @PatchMapping("/session/{id}/enable")
    public ResponseEntity<String> enableSession(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        sessionService.setSessionActive(id, userDetails.getUser().getId(), true);
        return ResponseEntity.ok("OK");
    }
}
