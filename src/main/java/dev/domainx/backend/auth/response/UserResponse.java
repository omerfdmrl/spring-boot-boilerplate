package dev.domainx.backend.auth.response;

import dev.domainx.backend.user.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserResponse {
    private String name;
    private String email;

    public UserResponse(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
    }
}
