package dev.nyom.backend.system.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.nyom.backend.exceptions.ErrorResponse;
import dev.nyom.backend.system.dto.SettingDto;
import dev.nyom.backend.system.mapper.SettingMapper;
import dev.nyom.backend.system.model.Setting;
import dev.nyom.backend.system.service.SettingService;
import dev.nyom.backend.user.impl.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
public class SettingController {
    private final SettingService settingService;
    private final SettingMapper settingMapper;

    @Operation(
        summary = "Get current user's settings",
        description = "Retrieves all settings specific to the currently authenticated user."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of user settings retrieved successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                array = @ArraySchema(schema = @Schema(implementation = SettingDto.class)))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - user is not authenticated",
            content = @Content)
    })
    @GetMapping("")
    public ResponseEntity<List<SettingDto>> getUserSettings(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<Setting> settings = settingService.getUserSettings(userDetails.getUser().getId());
        List<SettingDto> dtos = settings.stream()
                .map(settingMapper::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @Operation(
        summary = "Save a new setting for the current user",
        description = "Creates and saves a new setting for the authenticated user. The setting key must be unique per user."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Setting saved successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = SettingDto.class))),
        @ApiResponse(responseCode = "400", description = "Validation error or duplicate key",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - user is not authenticated",
            content = @Content)
    })
    @PostMapping("")
    public ResponseEntity<SettingDto> saveSetting(@Valid @RequestBody SettingDto dto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Setting setting = settingMapper.toEntity(dto, userDetails.getUser());
        Setting saved = settingService.saveSetting(setting);
        return ResponseEntity.ok(settingMapper.toDto(saved));
    }

    @Operation(
        summary = "Delete a setting by key",
        description = "Deletes a setting identified by its key for the currently authenticated user."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Setting deleted successfully",
            content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)),
        @ApiResponse(responseCode = "404", description = "Setting with the specified key not found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - user is not authenticated",
            content = @Content)
    })
    @DeleteMapping("/{key}")
    public ResponseEntity<String> deleteSetting(@PathVariable String key, @AuthenticationPrincipal CustomUserDetails userDetails) {
        settingService.deleteSetting(key, userDetails.getId());
        return ResponseEntity.ok("OK");
    }

    @Operation(
        summary = "Update a user setting by key",
        description = "Updates the value of an existing user setting identified by its key."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Setting updated successfully",
            content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)),
        @ApiResponse(responseCode = "404", description = "Setting with the specified key not found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - user is not authenticated",
            content = @Content)
    })
    @PatchMapping("/{key}")
    public ResponseEntity<String> updateSetting(@PathVariable String key, @RequestBody String value, @AuthenticationPrincipal CustomUserDetails userDetails) {
        this.settingService.updateUserSetting(userDetails.getUser().getId(), key, value);
        return ResponseEntity.ok("OK");
    }

    @Operation(
        summary = "Get system-wide settings",
        description = "Retrieves all settings which are system-wide and not user-specific."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of system settings retrieved successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                array = @ArraySchema(schema = @Schema(implementation = SettingDto.class)))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - user is not authenticated",
            content = @Content)
    })
    @GetMapping("/system")
    public ResponseEntity<List<SettingDto>> getSystemSettings() {
        List<Setting> settings = settingService.getSystemSettings();
        List<SettingDto> dtos = settings.stream()
                .map(settingMapper::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @Operation(
        summary = "Update a system-wide setting by key",
        description = "Updates the value of an existing system-wide setting identified by its key."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "System setting updated successfully",
            content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)),
        @ApiResponse(responseCode = "404", description = "Setting with the specified key not found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - user is not authenticated",
            content = @Content)
    })
    @PatchMapping("/system")
    public ResponseEntity<String> updateSystemSettings(@PathVariable String key, @RequestBody String value) {
        this.settingService.updateSystemSetting(key, value);
        return ResponseEntity.ok("OK");
    }
}
