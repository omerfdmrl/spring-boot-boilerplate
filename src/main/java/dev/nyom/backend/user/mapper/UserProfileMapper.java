package dev.nyom.backend.user.mapper;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import dev.nyom.backend.user.dto.UserProfileDto;
import dev.nyom.backend.user.model.UserProfile;

@Component
public class UserProfileMapper {
    public UserProfileDto toDto(UserProfile entity) {
        UserProfileDto dto = new UserProfileDto();
        dto.setBio(entity.getBio());
        dto.setAvatar(entity.getAvatar());
        dto.setWebsite(entity.getWebsite());
        dto.setLocation(entity.getLocation());
        dto.setSocialAccounts(entity.getSocialAccounts() == null ? null : new HashMap<>(entity.getSocialAccounts()));
        return dto;
    }

    public UserProfile toEntity(UserProfileDto dto) {
        UserProfile entity = new UserProfile();
        entity.setBio(dto.getBio());
        entity.setAvatar(dto.getAvatar());
        entity.setWebsite(dto.getWebsite());
        entity.setLocation(dto.getLocation());
        entity.setSocialAccounts(dto.getSocialAccounts());
        return entity;
    }
}
