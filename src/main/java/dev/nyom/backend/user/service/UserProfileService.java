package dev.nyom.backend.user.service;

import dev.nyom.backend.user.model.User;
import dev.nyom.backend.user.model.UserProfile;
import dev.nyom.backend.user.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;

    public Optional<UserProfile> findByUser(User user) {
        return userProfileRepository.findByUser(user);
    }

    public UserProfile save(UserProfile profile) {
        return userProfileRepository.save(profile);
    }
}
