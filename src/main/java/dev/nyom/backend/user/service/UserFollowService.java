package dev.nyom.backend.user.service;

import dev.nyom.backend.exceptions.ErrorCodes;
import dev.nyom.backend.exceptions.GlobalException;
import dev.nyom.backend.user.model.User;
import dev.nyom.backend.user.model.UserFollow;
import dev.nyom.backend.user.repository.UserFollowRepository;
import dev.nyom.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserFollowService {
    private final UserFollowRepository userFollowRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<UserFollow> getFollowers(Long userId) {
        return userFollowRepository.findByFollowingId(userId);
    }

    @Transactional(readOnly = true)
    public List<UserFollow> getFollowing(Long userId) {
        return userFollowRepository.findByFollowerId(userId);
    }

    @Transactional(readOnly = true)
    public List<UserFollow> getFollowers(String targetUsername) {
        User targetUser = this.userRepository.findByUsername(targetUsername)
            .orElseThrow(() -> new GlobalException(ErrorCodes.USER_NOT_FOUND));
        return userFollowRepository.findByFollowingId(targetUser.getId());
    }

    @Transactional(readOnly = true)
    public List<UserFollow> getFollowing(String targetUsername) {
        User targetUser = this.userRepository.findByUsername(targetUsername)
            .orElseThrow(() -> new GlobalException(ErrorCodes.USER_NOT_FOUND));
        return userFollowRepository.findByFollowerId(targetUser.getId());
    }

    @Transactional
    public UserFollow followUser(User follower, String targetUsername) {
        User targetUser = this.userRepository.findByUsername(targetUsername)
            .orElseThrow(() -> new GlobalException(ErrorCodes.USER_NOT_FOUND));
        UserFollow userFollow = new UserFollow();
        userFollow.setFollower(follower);
        userFollow.setFollowing(targetUser);
        return userFollowRepository.save(userFollow);
    }

    @Transactional
    public void unfollowUser(Long followerId, String targetUsername) {
        User targetUser = this.userRepository.findByUsername(targetUsername)
            .orElseThrow(() -> new GlobalException(ErrorCodes.USER_NOT_FOUND));

        this.userFollowRepository.deleteByFollowerIdAndFollowingId(followerId, targetUser.getId());
    }

    @Transactional(readOnly = true)
    public boolean isFollowing(Long followerId, Long followingId) {
        return userFollowRepository.existsByFollowerIdAndFollowingId(followerId, followingId);
    }
}
