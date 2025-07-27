package dev.nyom.backend.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Schema(description = "Data Transfer Object for User Follow relationship")
public class UserFollowDto {
    @Schema(description = "ID of the follower user", example = "3")
    private UserDto follower;

    @Schema(description = "ID of the followed user", example = "5")
    private UserDto following;
}
