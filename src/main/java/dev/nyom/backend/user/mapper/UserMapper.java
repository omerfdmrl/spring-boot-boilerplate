package dev.nyom.backend.user.mapper;

import org.springframework.stereotype.Component;

import dev.nyom.backend.user.dto.UserDto;
import dev.nyom.backend.user.model.User;

@Component
public class UserMapper {
    public UserDto toDto(User entity) {
        UserDto dto = new UserDto();
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        dto.setUsername(entity.getUsername());
        return dto;
    }

    public User toEntity(UserDto dto) {
        User entity = new User();
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setUsername(dto.getUsername());
        return entity;
    }
}
