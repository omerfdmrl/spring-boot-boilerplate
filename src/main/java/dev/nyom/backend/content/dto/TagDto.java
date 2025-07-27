package dev.nyom.backend.content.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Schema(description = "Represents a tag for contents.")
public class TagDto {
    @Schema(description = "Title of tag", example = "Front End")
    private String title;

    @Schema(description = "Slug of tag", example = "front-end")
    private String slug;

    @Schema(description = "Description of tag", example = "Worst programming area")
    private String description;

    @Schema(description = "Icon of tag display", example = "svg-icon")
    private String icon;

    @Schema(description = "Color of tag display", example = "blue")
    private String color;

    @Schema(description = "Is tag active", example = "True")
    private Boolean isActive;
}
