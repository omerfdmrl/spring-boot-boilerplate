package dev.nyom.backend.user.dto.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.nyom.backend.exceptions.ErrorCodes;
import dev.nyom.backend.exceptions.ErrorResponse;
import dev.nyom.backend.exceptions.GlobalException;
import dev.nyom.backend.user.dto.UserProfileDto;
import dev.nyom.backend.user.impl.CustomUserDetails;
import dev.nyom.backend.user.mapper.UserProfileMapper;
import dev.nyom.backend.user.model.User;
import dev.nyom.backend.user.model.UserProfile;
import dev.nyom.backend.user.service.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user/profile")
@RequiredArgsConstructor
public class UserProfileController {
    private final UserProfileService userProfileService;
    private final UserProfileMapper userProfileMapper;

    @Operation(
        summary = "Get current user's profile",
        description = "Returns the profile information of the currently authenticated user",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "User profile retrieved successfully",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserProfileDto.class)
                )
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Unauthorized - user is not authenticated",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Profile not found for the authenticated user",
                content = @Content
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
                )
            )
        }
    )
    @GetMapping
    public ResponseEntity<UserProfileDto> getProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        UserProfile userProfile = this.userProfileService.findByUser(user)
                    .orElseThrow(() -> new GlobalException(ErrorCodes.INTERNAL_SERVER_ERROR));
        UserProfileDto userProfileDto = this.userProfileMapper.toDto(userProfile);
        return ResponseEntity.ok(userProfileDto);
    }

    @Operation(
        summary = "Update current user's profile",
        description = "Updates the profile information of the currently authenticated user. " +
                      "Only fields present in the request will be updated.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Profile data to update",
            required = true,
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = UserProfileDto.class)
            )
        ),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "User profile updated successfully",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserProfileDto.class)
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Validation error in profile update data",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Unauthorized - user is not authenticated",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
                )
            )
        }
    )
    @PutMapping
    public ResponseEntity<String> updateProfile(@AuthenticationPrincipal CustomUserDetails userDetails, @Valid @RequestBody UserProfileDto profileDto) {
        User user = userDetails.getUser();
        UserProfile profile = this.userProfileService.findByUser(user)
                .orElseGet(() -> {
                    UserProfile newProfile = new UserProfile();
                    newProfile.setUser(user);
                    return newProfile;
                });

        profile.setBio(profileDto.getBio());
        profile.setAvatar(profileDto.getAvatar());
        profile.setWebsite(profileDto.getWebsite());
        profile.setLocation(profileDto.getLocation());
        profile.setSocialAccounts(profileDto.getSocialAccounts());

        this.userProfileService.save(profile);
        return ResponseEntity.ok("OK");
    }
}
