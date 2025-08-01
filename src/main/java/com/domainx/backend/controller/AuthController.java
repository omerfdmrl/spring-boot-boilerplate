package com.domainx.backend.controller;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
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

import com.domainx.backend.dto.ErrorResponse;
import com.domainx.backend.dto.ForgotPasswordRequest;
import com.domainx.backend.dto.LoginRequest;
import com.domainx.backend.dto.RefreshRequest;
import com.domainx.backend.dto.RegisterRequest;
import com.domainx.backend.dto.ResetPasswordRequest;
import com.domainx.backend.dto.TokenResponse;
import com.domainx.backend.model.Token;
import com.domainx.backend.model.User;
import com.domainx.backend.repository.TokenRepository;
import com.domainx.backend.repository.UserRepository;
import com.domainx.backend.security.JWTService;
import com.domainx.backend.service.MailService;
import com.domainx.backend.service.TokenService;
import com.domainx.backend.service.UserDetailsServiceImpl;
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
    @Autowired
    private MessageSource messageSource;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, Locale locale) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            TokenResponse userTokens = tokenService.generateJwtTokens(userDetails);
            return ResponseEntity.ok(userTokens);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse(messageSource.getMessage("invalid_credentials", null, locale)));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest, Locale locale) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(messageSource.getMessage("email_already_taken", null, locale)));
        }
        User newUser = new User(
            registerRequest.getName(),
            RandomUtils.getSaltString(),
            registerRequest.getEmail(),
            encoder.encode(registerRequest.getPassword()), null
        );
        userRepository.save(newUser);

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
            newUser.getEmail(),
            newUser.getPassword(),
            List.of()
        );

        return ResponseEntity.ok(new TokenResponse(jwtService.generateAccessToken(userDetails), jwtService.generateRefreshToken(userDetails)));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshRequest refreshRequest, Locale locale) {
        try {
            String refreshToken = refreshRequest.getRefreshToken();
            String email = jwtService.extractSubject(refreshToken);
        
            UserDetails userDetails = userDetailsService.loadUserByEmail(email);

            if (!jwtService.isTokenValid(refreshToken, userDetails, "REFRESH")) {
                return ResponseEntity.badRequest().body(new ErrorResponse(messageSource.getMessage("invalid_token", null, locale)));
            }

            String newAccessToken = jwtService.generateAccessToken(userDetails);

            return ResponseEntity.ok(new TokenResponse(newAccessToken, refreshToken));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(messageSource.getMessage("invalid_token", null, locale)));
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest, Locale locale) {
        try {
            String email = forgotPasswordRequest.getEmail();
            UserDetails userDetails = userDetailsService.loadUserByEmail(email);
            String token = tokenService.generatePasswordResetToken(userDetails);
            mailService.sendPasswordResetEmail(email, "/" + token, locale);
            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            // Add Logger
            return ResponseEntity.ok("OK");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest, Locale locale) {
        try {
            String token = resetPasswordRequest.getToken();
            Optional<Token> token_doc = tokenRepository.findByToken(token);
            String email = jwtService.extractSubject(token);
            String tokenType = "RESET_PASSWORD";

            if (token_doc.isEmpty() ||
                jwtService.isTokenExpired(token) ||
                !tokenType.equals(jwtService.extractTokenType(token))) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(messageSource.getMessage("invalid_token", null, locale)));
            }

            Optional<User> userOptional = userRepository.findByEmail(email);
            if (userOptional.isPresent()) {
                User user = userOptional.get();

                user.setPassword(encoder.encode(resetPasswordRequest.getPassword()));
                userRepository.save(user);

                tokenRepository.delete(token_doc.get());
            }

            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(messageSource.getMessage("invalid_token", null, locale)));
        }
    }
}
