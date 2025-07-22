package com.domainx.backend.user.dto;

import java.util.Set;

import com.domainx.backend.user.models.Role;

import lombok.Data;

@Data
public class UserDTO {
    private String name;
    private String username;
    private String email;
    private Set<Role> roles;
}
