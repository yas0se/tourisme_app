package com.gestion.tourisme_app.service;

import com.gestion.tourisme_app.dto.CategoryDto;
import com.gestion.tourisme_app.entity.Category;
import com.gestion.tourisme_app.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Cacheable("categories")
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findByIsActiveTrueOrderByTitleAsc()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return convertToDto(category);
    }

    private CategoryDto convertToDto(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setTitle(category.getTitle());
        dto.setDescription(category.getDescription());
        dto.setImageUrl(category.getImageUrl());
        dto.setIsActive(category.getIsActive());
        dto.setCreatedDate(category.getCreatedDate());
        dto.setUpdatedDate(category.getUpdatedDate());
        return dto;
    }
}
