package dev.nyom.backend.user.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import dev.nyom.backend.exceptions.ErrorCodes;
import dev.nyom.backend.exceptions.GlobalException;
import dev.nyom.backend.user.impl.CustomUserDetails;
import dev.nyom.backend.user.model.User;

@Component
public class UserService {
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new GlobalException(ErrorCodes.AUTH_INVALID_CREDENTIALS);
        }

        Object principal = auth.getPrincipal();
        if (principal instanceof CustomUserDetails userDetails) {
            return userDetails.getUser();
        }

        throw new GlobalException(ErrorCodes.AUTH_USER_NOT_FOUND);
    }
}
