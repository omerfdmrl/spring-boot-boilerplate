package dev.nyom.backend.article.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import dev.nyom.backend.content.model.Category;
import dev.nyom.backend.user.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "articles")
@Schema(description = "Represents the article.")
public class Article {
    public enum ArticleStatus {
        DRAFT("DRAFT"),
        PUBLISH("PUBLISH");

        private final String value;

        ArticleStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static ArticleStatus fromValue(String value) {
            for (ArticleStatus type : values()) {
                if (type.value.equalsIgnoreCase(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown session type: " + value);
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the article", example = "1")
    private Long id;

    @Column(nullable = false, length = 255)
    @Schema(description = "Title of article", example = "Back End Is Best?")
    private String title;

    @Column(nullable = false, length = 255, unique = true)
    @Schema(description = "Slug of article", example = "back-end-is-best?")
    private String slug;

    @Column(nullable = false, columnDefinition = "TEXT")
    @Schema(description = "Excerpt of article", example = "Yes, it is best.")
    private String excerpt;

    @Column(nullable = false, columnDefinition = "TEXT")
    @Schema(description = "Content of article", example = "Yes, it is best, because ...")
    private String content;

    @Column(nullable = false, length = 255)
    @Schema(description = "Image of article display", example = "svg-icon")
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    @Schema(description = "The author this article is associated with")
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @Schema(description = "The category of article")
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Status of the article (e.g., DRAFT, PUBLISH)", example = "DRAFT")
    private ArticleStatus status;
    
    @Column(name = "is_featured")
    @Schema(description = "Is article featured?", example = "true")
    private Boolean isFeatured = false;
    
    @Column(name = "is_pinned")
    @Schema(description = "Is article pinned?", example = "false")
    private Boolean isPinned = false;
    
    @Column(name = "read_time")
    @Schema(description = "The reading time of article?", example = "03:58")
    private Integer readTime;

    @Column(name = "published_at")
    @Schema(description = "Date and time when the article was/will published", example = "2025-07-22T10:00:00")
    private LocalDateTime publishedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    @Schema(description = "Date and time when the article was created", example = "2025-07-22T10:00:00")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    @Schema(description = "Date and time when the article was last updated", example = "2025-07-22T12:30:00")
    private LocalDateTime updatedAt;
}
