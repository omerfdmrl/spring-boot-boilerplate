package dev.nyom.backend.user.dto.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.nyom.backend.exceptions.ErrorResponse;
import dev.nyom.backend.user.dto.UserDto;
import dev.nyom.backend.user.impl.CustomUserDetails;
import dev.nyom.backend.user.mapper.UserMapper;
import dev.nyom.backend.user.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserMapper userMapper;

    @Operation(
        summary = "Get current authenticated user info",
        description = "Returns the basic information of the currently authenticated user."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "User information retrieved successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = UserDto.class)
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
    @GetMapping("/me")
    public ResponseEntity<UserDto> me(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        UserDto userDto = this.userMapper.toDto(user);
        return ResponseEntity.ok(userDto);
    }
}
