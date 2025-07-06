package com.domainx.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.domainx.backend.dto.LoginRequest;
import com.domainx.backend.dto.RefreshRequest;
import com.domainx.backend.dto.RegisterRequest;
import com.domainx.backend.dto.TokenResponse;
import com.domainx.backend.model.User;
import com.domainx.backend.repository.UserRepository;
import com.domainx.backend.security.JWTService;
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
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            TokenResponse userTokens = tokenService.generateJwtTokens(userDetails);
            return ResponseEntity.ok(userTokens);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("Invalid username or password"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity
                .badRequest()
                .body(new ErrorResponse("Username is already taken"));
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
    public ResponseEntity<?> refreshToken(@RequestBody RefreshRequest refreshRequest) {
        try {
            String refreshToken = refreshRequest.getRefreshToken();
            String email = jwtService.extractSubject(refreshToken);
        
            UserDetails userDetails = userDetailsService.loadUserByEmail(email);

            if (!jwtService.isTokenValid(refreshToken, userDetails)) {
                return ResponseEntity.badRequest().body(new ErrorResponse("Token is not valid"));
            }

            String newAccessToken = jwtService.generateAccessToken(userDetails);

            return ResponseEntity.ok(new TokenResponse(newAccessToken, refreshToken));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("Invalid refresh token"));
        }
    }
}
