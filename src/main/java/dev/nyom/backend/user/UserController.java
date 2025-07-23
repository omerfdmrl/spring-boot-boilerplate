package dev.nyom.backend.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.nyom.backend.exceptions.ErrorCodes;
import dev.nyom.backend.exceptions.GlobalException;
import dev.nyom.backend.user.dto.UserDto;
import dev.nyom.backend.user.mapper.UserMapper;
import dev.nyom.backend.user.model.User;
import dev.nyom.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<UserDto> me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new GlobalException(ErrorCodes.AUTH_INVALID_CREDENTIALS);
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new GlobalException(ErrorCodes.AUTH_USER_NOT_FOUND));

        UserDto userDto = UserMapper.toDto(user);
        return ResponseEntity.ok(userDto);
    }
}
