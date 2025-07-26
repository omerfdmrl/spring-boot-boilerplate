package dev.nyom.backend.user.impl;

import java.time.Instant;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import dev.nyom.backend.user.model.User;

public class CustomUserDetails implements UserDetails {
    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                .collect(Collectors.toSet());
    }

    @Override public String getPassword() {
        return user.getPassword();
    }

    @Override public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !user.isLocked() && !user.isBlocked();
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.getAccountExpireAt() == null || user.getAccountExpireAt().isAfter(Instant.now());
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.getCredentialsExpireAt() == null || user.getCredentialsExpireAt().isAfter(Instant.now());
    }
}
