package dev.nyom.backend.user.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import dev.nyom.backend.exceptions.ErrorCodes;
import dev.nyom.backend.exceptions.GlobalException;
import dev.nyom.backend.user.impl.UserDetailsImpl;
import dev.nyom.backend.user.model.User;

@Component
public class CurrentUserService {

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new GlobalException(ErrorCodes.AUTH_INVALID_CREDENTIALS);
        }

        Object principal = auth.getPrincipal();
        if (principal instanceof UserDetailsImpl userDetails) {
            return userDetails.getUser();
        }

        throw new GlobalException(ErrorCodes.AUTH_USER_NOT_FOUND);
    }
}
