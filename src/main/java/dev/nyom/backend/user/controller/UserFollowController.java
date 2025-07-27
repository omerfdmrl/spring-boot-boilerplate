package dev.nyom.backend.user.controller;

import dev.nyom.backend.exceptions.ErrorCodes;
import dev.nyom.backend.exceptions.ErrorResponse;
import dev.nyom.backend.exceptions.GlobalException;
import dev.nyom.backend.user.dto.UserFollowDto;
import dev.nyom.backend.user.model.User;
import dev.nyom.backend.user.impl.CustomUserDetails;
import dev.nyom.backend.user.mapper.UserFollowMapper;
import dev.nyom.backend.user.service.UserFollowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.media.*;
import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserFollowController {
    private final UserFollowService userFollowService;
    private final UserFollowMapper userFollowMapper;

    @Operation(
        summary = "Get Followers",
        description = "Get list of users who follow the current user."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "List of followers retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = UserFollowDto.class))
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
    })
    @GetMapping("/me/followers")
    public ResponseEntity<List<UserFollowDto>> getFollowers(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<UserFollowDto> followers = userFollowService.getFollowers(userDetails.getId())
                .stream()
                .map(userFollowMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(followers);
    }

    @Operation(
        summary = "Get Followings",
        description = "Get list of users the current user is following."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "List of following users retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = UserFollowDto.class))
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
    })
    @GetMapping("/me/following")
    public ResponseEntity<List<UserFollowDto>> getFollowing(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<UserFollowDto> following = userFollowService.getFollowing(userDetails.getUser().getId())
                .stream()
                .map(userFollowMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(following);
    }

    @Operation(
        summary = "Follow User",
        description = "Follow a user by their username."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully followed the user",
            content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Bad request (e.g. user tries to follow themselves)",
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
            responseCode = "404",
            description = "Not Found - User to follow not found",
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
    })
    @PostMapping("/{username}/follow")
    public ResponseEntity<String> followUser(@PathVariable String username, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails.getUser().getUsername().equals(username)) {
            throw new GlobalException(ErrorCodes.USER_CONNOT_FOLLOW_YOURSELF);
        }

        userFollowService.followUser(userDetails.getUser(), username);
        return ResponseEntity.ok("OK");
    }

    @Operation(
        summary = "Unfollow User",
        description = "Unfollow a user by their username."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully unfollowed the user",
            content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Bad request (e.g. user tries to follow themselves)",
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
            responseCode = "404",
            description = "Not Found - User to follow not found",
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
    })
    @DeleteMapping("/{username}/unfollow")
    public ResponseEntity<String> unfollowUser(@PathVariable String username, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails.getUser().getUsername().equals(username)) {
            throw new GlobalException(ErrorCodes.USER_CONNOT_FOLLOW_YOURSELF);
        }
        User currentUser = userDetails.getUser();
        userFollowService.unfollowUser(currentUser.getId(), username);
        return ResponseEntity.ok("OK");
    }

    @Operation(
        summary = "Get Followers",
        description = "Get list of users follower by username."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "List of followers retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = UserFollowDto.class))
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
            description = "Not Found - User not found",
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
    })
    @GetMapping("/{username}/followers")
    public ResponseEntity<List<UserFollowDto>> getUserFollowers(@PathVariable String username, @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<UserFollowDto> followers = userFollowService.getFollowers(username)
                .stream()
                .map(userFollowMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(followers);
    }

    @Operation(
        summary = "Get Followings",
        description = "Get list of users is following by username."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "List of following users retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = UserFollowDto.class))
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
            description = "Not Found - User not found",
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
    })
    @GetMapping("/{username}/following")
    public ResponseEntity<List<UserFollowDto>> getUserFollowing(@PathVariable String username, @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<UserFollowDto> following = userFollowService.getFollowing(username)
                .stream()
                .map(userFollowMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(following);
    }
}
