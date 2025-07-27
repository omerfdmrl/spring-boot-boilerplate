package dev.nyom.backend.user.mapper;

import dev.nyom.backend.user.dto.UserFollowDto;
import dev.nyom.backend.user.model.UserFollow;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFollowMapper {

    public UserFollowDto toDto(UserFollow entity) {
        UserFollowDto dto = new UserFollowDto();
        dto.setFollower(entity.getFollower());
        dto.setFollowing(entity.getFollowing());
        return dto;
    }

    public UserFollow toEntity(UserFollowDto dto) {
        UserFollow entity = new UserFollow();
        entity.setFollower(dto.getFollower());
        entity.setFollowing(dto.getFollowing());
        return entity;
    }
}
