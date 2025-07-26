package dev.nyom.backend.user.dto;

import java.io.Serializable;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserProfileDto implements Serializable{
    @Schema(description = "User's biography information", example = "I am the best developer so far")
    private String bio;

    @Schema(description = "User's avatar image as uri", example = "https://example.com/avatar.png")
    private String avatar;

    @Schema(description = "User's owned website", example = "https://example.com")
    private String website;

    @Schema(description = "User's location", example = "Planet Earth")
    private String location;

    @Schema(description = "User's social accounts", example = "{\"github\":\"foobar\", \"twitter\":\"foobar\"}")
    private Map<String, String> socialAccounts;
}
