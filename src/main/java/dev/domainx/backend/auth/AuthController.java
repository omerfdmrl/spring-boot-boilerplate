package dev.domainx.backend.auth;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.domainx.backend.auth.request.ForgotPasswordRequest;
import dev.domainx.backend.auth.request.LoginRequest;
import dev.domainx.backend.auth.request.RefreshRequest;
import dev.domainx.backend.auth.request.RegisterRequest;
import dev.domainx.backend.auth.request.ResetPasswordRequest;
import dev.domainx.backend.auth.response.TokenResponse;
import dev.domainx.backend.auth.response.UserResponse;
import dev.domainx.backend.auth.response.UserTokenResponse;
import dev.domainx.backend.exceptions.ErrorCodes;
import dev.domainx.backend.exceptions.ErrorResponse;
import dev.domainx.backend.exceptions.GlobalException;
import dev.domainx.backend.notification.MailService;
import dev.domainx.backend.security.JWTService;
import dev.domainx.backend.user.dto.UserDetailsServiceImpl;
import dev.domainx.backend.user.models.User;
import dev.domainx.backend.user.repository.UserRepository;
import dev.domainx.backend.utils.RandomUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private MailService mailService;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private PasswordEncoder encoder;
    
    @Operation(
        summary = "User Login",
        description = "Authenticate user by email and password and return JWT tokens with user info."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful login",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = UserTokenResponse.class))),
        
        @ApiResponse(responseCode = "400", description = "Invalid input or validation error",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class))),

        @ApiResponse(responseCode = "401", description = "Authentication failed - invalid credentials or user disabled/locked/expired",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class))),

        @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class))),

        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<UserTokenResponse> login(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Login request containing email and password",
            required = true,
            content = @Content(schema = @Schema(implementation = LoginRequest.class))
        ) @Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        User user = userRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new GlobalException(ErrorCodes.AUTH_USER_NOT_FOUND));

        UserResponse userResponse = new UserResponse(user);
        TokenResponse tokenResponse = tokenService.generateJwtTokens(userDetails.getUsername());
        UserTokenResponse userTokenResponse = new UserTokenResponse(tokenResponse, userResponse);

        return ResponseEntity.ok(userTokenResponse);
    }

    @Operation(
        summary = "User registration",
        description = "Registers a new user with name, email, and password. Returns JWT tokens and user info on success."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registration successful",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = UserTokenResponse.class))),
        
        @ApiResponse(responseCode = "400", description = "Validation failed (e.g. missing fields)",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class))),

        @ApiResponse(responseCode = "409", description = "Email already in use (AUTH_EMAIL_TAKEN)",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class))),

        @ApiResponse(responseCode = "500", description = "Unexpected internal error",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<UserTokenResponse> register(
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Register request containing name, email, and password",
        required = true,
        content = @Content(schema = @Schema(implementation = RegisterRequest.class))
    ) @Valid @RequestBody RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new GlobalException(ErrorCodes.AUTH_EMAIL_TAKEN);
        }
        User newUser = new User(
            registerRequest.getName(),
            RandomUtils.getSaltString(),
            registerRequest.getEmail(),
            encoder.encode(registerRequest.getPassword()), null
        );
        userRepository.save(newUser);

        TokenResponse tokenResponse = tokenService.generateJwtTokens(newUser.getEmail());
        UserResponse userResponse = new UserResponse(newUser);
        UserTokenResponse userTokenResponse = new UserTokenResponse(tokenResponse, userResponse);

        return ResponseEntity.ok(userTokenResponse);
    }

    @Operation(
        summary = "Refresh JWT token",
        description = "Generates a new access token using a valid refresh token"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "New access token generated",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = TokenResponse.class))),

        @ApiResponse(responseCode = "400", description = "Validation error in request body",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class))),

        @ApiResponse(responseCode = "401", description = "Refresh token is invalid, expired, or mismatched",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class))),

        @ApiResponse(responseCode = "404", description = "User not found (email in token not registered)",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class))),

        @ApiResponse(responseCode = "500", description = "Unexpected internal server error",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshToken(
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Refresh request containing the refresh token",
        required = true,
        content = @Content(schema = @Schema(implementation = RefreshRequest.class))
    ) @Valid @RequestBody RefreshRequest refreshRequest) {
        String refreshToken = refreshRequest.getRefreshToken();
        String email;

        try {
            email = jwtService.extractSubject(refreshToken);
        } catch (Exception e) {
            throw new GlobalException(ErrorCodes.AUTH_TOKEN_INVALID);
        }
    
        UserDetails userDetails = userDetailsService.loadUserByEmail(email);

        if (!jwtService.isTokenValid(refreshToken, userDetails, "REFRESH")) {
            throw new GlobalException(ErrorCodes.AUTH_TOKEN_INVALID);
        }

        String newAccessToken = jwtService.generateAccessToken(userDetails.getUsername());
        TokenResponse tokenResponse = new TokenResponse(newAccessToken, refreshToken);

        return ResponseEntity.ok(tokenResponse);
    }

    @Operation(
        summary = "Forgot password",
        description = "Sends a password reset email to the user if the email exists in the system."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Password reset email sent (or suppressed silently if user not found)",
            content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)),

        @ApiResponse(responseCode = "400", description = "Validation error (invalid or missing email)",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class))),

        @ApiResponse(responseCode = "500", description = "Unexpected internal server error (mail service, token generation failure, etc.)",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Request containing the user's email to initiate password reset",
        required = true,
        content = @Content(schema = @Schema(implementation = ForgotPasswordRequest.class))
    ) @Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest, Locale locale) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByEmail(forgotPasswordRequest.getEmail());
            String token = tokenService.generatePasswordResetToken(userDetails.getUsername());
            mailService.sendPasswordResetEmail(userDetails.getUsername(), "/" + token, locale);
        } catch (Exception e) {
            // Add Logger
        }
        return ResponseEntity.ok("OK");
    }

    @Operation(
        summary = "Reset password",
        description = "Resets the user password using a valid reset token and new password."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Password successfully reset",
            content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)),

        @ApiResponse(responseCode = "400", description = "Validation failed (e.g. missing fields)",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class))),

        @ApiResponse(responseCode = "401", description = "Reset token is invalid or expired",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class))),

        @ApiResponse(responseCode = "404", description = "User not found (email in token not found)",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class))),

        @ApiResponse(responseCode = "500", description = "Unexpected internal error",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Request containing reset token and new password",
        required = true,
        content = @Content(schema = @Schema(implementation = ResetPasswordRequest.class))
    ) @Valid @RequestBody ResetPasswordRequest request) {
        String token = request.getToken();
        Token tokenDoc = tokenRepository.findByToken(token)
            .orElseThrow(() -> new GlobalException(ErrorCodes.AUTH_RESET_TOKEN_INVALID));

        if (jwtService.isTokenExpired(token)
                || !"RESET_PASSWORD".equals(jwtService.extractTokenType(token))) {
            throw new GlobalException(ErrorCodes.AUTH_RESET_TOKEN_INVALID);
        }

        String email;
        try {
            email = jwtService.extractSubject(token);
        } catch (Exception e) {
            throw new GlobalException(ErrorCodes.AUTH_RESET_TOKEN_INVALID);
        }

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new GlobalException(ErrorCodes.AUTH_USER_NOT_FOUND));

        user.setPassword(encoder.encode(request.getPassword()));
        userRepository.save(user);
        tokenRepository.delete(tokenDoc);

        return ResponseEntity.ok("OK");
    }
}
