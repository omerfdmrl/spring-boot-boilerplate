package dev.nyom.backend.user.mapper;

import org.springframework.stereotype.Component;

import dev.nyom.backend.user.dto.UserDto;
import dev.nyom.backend.user.model.User;

@Component
public class UserMapper {
    public UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        return dto;
    }

    public User toEntity(UserDto dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
        return user;
    }
}
