package dev.nyom.backend.article.mapper;

import org.springframework.stereotype.Component;

import dev.nyom.backend.article.dto.ArticleDto;
import dev.nyom.backend.article.model.Article;
import dev.nyom.backend.content.mapper.CategoryMapper;
import dev.nyom.backend.user.mapper.UserMapper;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ArticleMapper {
    private final UserMapper userMapper;
    private final CategoryMapper categoryMapper;

    public ArticleDto toDto(Article entity) {
        ArticleDto dto = new ArticleDto();
        dto.setTitle(entity.getTitle());
        dto.setSlug(entity.getSlug());
        dto.setExcerpt(entity.getExcerpt());
        dto.setContent(entity.getContent());
        dto.setImage(entity.getImage());
        dto.setAuthor(this.userMapper.toDto(entity.getAuthor()));
        dto.setCategory(this.categoryMapper.toDto(entity.getCategory()));
        dto.setStatus(entity.getStatus());
        dto.setIsFeatured(entity.getIsFeatured());
        dto.setIsPinned(entity.getIsPinned());
        return dto;
    }

    public Article toDto(ArticleDto dto) {
        Article entity = new Article();
        entity.setTitle(dto.getTitle());
        entity.setSlug(dto.getSlug());
        entity.setExcerpt(dto.getExcerpt());
        entity.setContent(dto.getContent());
        entity.setImage(dto.getImage());
        entity.setAuthor(this.userMapper.toEntity(dto.getAuthor()));
        entity.setCategory(this.categoryMapper.toEntity(dto.getCategory()));
        entity.setStatus(dto.getStatus());
        entity.setIsFeatured(dto.getIsFeatured());
        entity.setIsPinned(dto.getIsPinned());
        return entity;
    }
}
