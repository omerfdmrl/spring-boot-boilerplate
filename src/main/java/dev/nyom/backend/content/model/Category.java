package dev.nyom.backend.content.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "categories")
@Schema(description = "Represents a category for contents.")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the category", example = "1")
    private Long id;

    @Column(nullable = false, length = 255)
    @Schema(description = "Title of category", example = "Front End")
    private String title;

    @Column(nullable = false, unique = true, length = 255)
    @Schema(description = "Slug of category", example = "front-end")
    private String slug;

    @Column(nullable = false, columnDefinition = "TEXT")
    @Schema(description = "Description of category", example = "Worst programming area")
    private String description;

    @Column(length = 255)
    @Schema(description = "Icon of category display", example = "svg-icon")
    private String icon;

    @Column(length = 50)
    @Schema(description = "Color of category display", example = "blue")
    private String color;

    @Column(name = "is_active")
    @Schema(description = "Is category active", example = "True")
    private Boolean isActive;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    @Schema(description = "Date and time when the category was created", example = "2025-07-22T10:00:00")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    @Schema(description = "Date and time when the category was last updated", example = "2025-07-22T12:30:00")
    private LocalDateTime updatedAt;
}
