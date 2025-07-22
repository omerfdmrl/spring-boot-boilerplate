package com.domainx.backend.user.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.domainx.backend.exceptions.ErrorCodes;
import com.domainx.backend.exceptions.GlobalException;
import com.domainx.backend.user.dto.UserDetailsImpl;
import com.domainx.backend.user.models.User;

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
