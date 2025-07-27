package dev.nyom.backend.content.mapper;

import org.springframework.stereotype.Component;

import dev.nyom.backend.content.dto.TagDto;
import dev.nyom.backend.content.model.Tag;

@Component
public class TagMapper {
    public TagDto toDto(Tag entity) {
        TagDto dto = new TagDto();
        dto.setTitle(entity.getTitle());
        dto.setSlug(entity.getSlug());
        dto.setDescription(entity.getDescription());
        dto.setIcon(entity.getIcon());
        dto.setColor(entity.getColor());
        dto.setIsActive(entity.getIsActive());
        return dto;
    }

    public Tag toEntity(TagDto dto) {
        Tag entity = new Tag();
        entity.setTitle(dto.getTitle());
        entity.setSlug(dto.getSlug());
        entity.setDescription(dto.getDescription());
        entity.setIcon(dto.getIcon());
        entity.setColor(dto.getColor());
        entity.setIsActive(dto.getIsActive());
        return entity;
    }
}
