package dev.nyom.backend.content.service;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.nyom.backend.content.dto.TagDto;
import dev.nyom.backend.content.mapper.TagMapper;
import dev.nyom.backend.content.repository.TagRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    public List<TagDto> listAll() {
        return this.tagRepository.findAllByIsActive(true)
            .stream().map(tag -> this.tagMapper.toDto(tag)).toList();
    }
}
