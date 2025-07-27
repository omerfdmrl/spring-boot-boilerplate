package dev.nyom.backend.content.service;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.nyom.backend.content.dto.CategoryDto;
import dev.nyom.backend.content.mapper.CategoryMapper;
import dev.nyom.backend.content.repository.CategoryRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public List<CategoryDto> listAll() {
        return this.categoryRepository.findAllByIsActive(true)
            .stream().map(category -> this.categoryMapper.toDto(category)).toList();
    }
}
