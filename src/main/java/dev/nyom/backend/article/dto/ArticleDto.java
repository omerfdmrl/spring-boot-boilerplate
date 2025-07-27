package dev.nyom.backend.article.dto;

import dev.nyom.backend.article.model.Article.ArticleStatus;
import dev.nyom.backend.content.dto.CategoryDto;
import dev.nyom.backend.user.dto.UserDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ArticleDto {
    @Schema(description = "Title of article", example = "Back End Is Best?")
    private String title;

    @Schema(description = "Slug of article", example = "back-end-is-best?")
    private String slug;

    @Schema(description = "Excerpt of article", example = "Yes, it is best.")
    private String excerpt;

    @Schema(description = "Content of article", example = "Yes, it is best, because ...")
    private String content;

    @Schema(description = "Image of article display", example = "svg-icon")
    private String image;

    @Schema(description = "The author this article is associated with")
    private UserDto author;

    @Schema(description = "The category of article")
    private CategoryDto category;

    @Schema(description = "Status of the article (e.g., DRAFT, PUBLISH)", example = "DRAFT")
    private ArticleStatus status;
    
    @Schema(description = "Is article featured?", example = "true")
    private Boolean isFeatured = false;
    
    @Schema(description = "Is article pinned?", example = "false")
    private Boolean isPinned = false;
    
    @Schema(description = "The reading time of article?", example = "03:58")
    private Integer readTime;
}
