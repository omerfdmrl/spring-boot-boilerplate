package dev.nyom.backend.user.mapper;

import dev.nyom.backend.user.dto.UserFollowDto;
import dev.nyom.backend.user.model.UserFollow;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFollowMapper {
    private final UserMapper userMapper;

    public UserFollowDto toDto(UserFollow entity) {
        UserFollowDto dto = new UserFollowDto();
        dto.setFollower(this.userMapper.toDto(entity.getFollower()));
        dto.setFollowing(this.userMapper.toDto(entity.getFollowing()));
        return dto;
    }

    public UserFollow toEntity(UserFollowDto dto) {
        UserFollow entity = new UserFollow();
        entity.setFollower(this.userMapper.toEntity(dto.getFollower()));
        entity.setFollowing(this.userMapper.toEntity(dto.getFollowing()));
        return entity;
    }
}
