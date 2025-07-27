package dev.nyom.backend.content.mapper;

import org.springframework.stereotype.Component;

import dev.nyom.backend.content.dto.CategoryDto;
import dev.nyom.backend.content.model.Category;

@Component
public class CategoryMapper {
    public CategoryDto toDto(Category entity) {
        CategoryDto dto = new CategoryDto();
        dto.setTitle(entity.getTitle());
        dto.setSlug(entity.getSlug());
        dto.setDescription(entity.getDescription());
        dto.setIcon(entity.getIcon());
        dto.setColor(entity.getColor());
        dto.setIsActive(entity.getIsActive());
        return dto;
    }

    public Category toEntity(CategoryDto dto) {
        Category entity = new Category();
        entity.setTitle(dto.getTitle());
        entity.setSlug(dto.getSlug());
        entity.setDescription(dto.getDescription());
        entity.setIcon(dto.getIcon());
        entity.setColor(dto.getColor());
        entity.setIsActive(dto.getIsActive());
        return entity;
    }
}
