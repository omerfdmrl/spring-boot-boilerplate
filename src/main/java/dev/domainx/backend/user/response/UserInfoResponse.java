package dev.domainx.backend.user.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserInfoResponse {
    private String name;
    private String email;
}
