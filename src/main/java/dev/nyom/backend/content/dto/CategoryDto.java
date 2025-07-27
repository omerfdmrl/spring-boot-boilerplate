package dev.nyom.backend.content.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Schema(description = "Represents a category for contents.")
public class CategoryDto {
    @Schema(description = "Title of category", example = "Front End")
    private String title;

    @Schema(description = "Slug of category", example = "front-end")
    private String slug;

    @Schema(description = "Description of category", example = "Worst programming area")
    private String description;

    @Schema(description = "Icon of category display", example = "svg-icon")
    private String icon;

    @Schema(description = "Color of category display", example = "blue")
    private String color;

    @Schema(description = "Is category active", example = "True")
    private Boolean isActive;
}
