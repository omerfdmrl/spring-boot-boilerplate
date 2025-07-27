package dev.nyom.backend.user.dto;

import dev.nyom.backend.user.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Schema(description = "Data Transfer Object for User Follow relationship")
public class UserFollowDto {
    @Schema(description = "ID of the follower user", example = "3")
    private User follower;

    @Schema(description = "ID of the followed user", example = "5")
    private User following;
}
