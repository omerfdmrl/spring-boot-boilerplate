package com.domainx.backend.auth;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.domainx.backend.auth.request.ForgotPasswordRequest;
import com.domainx.backend.auth.request.LoginRequest;
import com.domainx.backend.auth.request.RefreshRequest;
import com.domainx.backend.auth.request.RegisterRequest;
import com.domainx.backend.auth.request.ResetPasswordRequest;
import com.domainx.backend.auth.response.TokenResponse;
import com.domainx.backend.auth.response.UserResponse;
import com.domainx.backend.auth.response.UserTokenResponse;
import com.domainx.backend.exceptions.ErrorCodes;
import com.domainx.backend.exceptions.GlobalException;
import com.domainx.backend.notification.MailService;
import com.domainx.backend.security.JWTService;
import com.domainx.backend.user.dto.UserDetailsServiceImpl;
import com.domainx.backend.user.models.User;
import com.domainx.backend.user.repository.UserRepository;
import com.domainx.backend.utils.RandomUtils;

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
    
    @PostMapping("/login")
    public ResponseEntity<UserTokenResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
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

    @PostMapping("/register")
    public ResponseEntity<UserTokenResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
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

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshToken(@Valid @RequestBody RefreshRequest refreshRequest) {
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

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest, Locale locale) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByEmail(forgotPasswordRequest.getEmail());
            String token = tokenService.generatePasswordResetToken(userDetails.getUsername());
            mailService.sendPasswordResetEmail(userDetails.getUsername(), "/" + token, locale);
        } catch (Exception e) {
            // Add Logger
        }
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
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
