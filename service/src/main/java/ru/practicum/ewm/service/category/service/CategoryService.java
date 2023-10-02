package ru.practicum.ewm.service.category.service;

import ru.practicum.ewm.service.category.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto addCategory(CategoryDto categoryDto);

    void deleteCategory(Long id);

    CategoryDto updateCategory(CategoryDto categoryDto, Long id);

    List<CategoryDto> getAllCategories(int from, int size);

    CategoryDto getCategoryById(Long id);
}
