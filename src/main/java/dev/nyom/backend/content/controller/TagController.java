package dev.nyom.backend.content.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.nyom.backend.content.dto.TagDto;
import dev.nyom.backend.content.service.TagService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/tags")
@AllArgsConstructor
public class TagController {
    private final TagService tagService;

    @GetMapping("")
    public ResponseEntity<List<TagDto>> getAll() {
        return ResponseEntity.ok(this.tagService.listAll());
    }
}
