package ru.practicum.ewm.service.category.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.service.category.dto.CategoryDto;
import ru.practicum.ewm.service.category.dto.CategoryMapper;
import ru.practicum.ewm.service.category.entity.Category;
import ru.practicum.ewm.service.category.repository.CategoryRepository;
import ru.practicum.ewm.service.exception.ResourceNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {
        return CategoryMapper.toCategoryDto(CategoryMapper.toCategory(categoryDto));
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Long id) {
        Category current = categoryRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Category not found"));
        current.setName(categoryDto.getName());
        return CategoryMapper.toCategoryDto(categoryRepository.save(current));
    }

    @Override
    public List<CategoryDto> getAllCategories(int from, int size) {
        return categoryRepository.findAll(PageRequest.of(from, size)).getContent().stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        Category current = categoryRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Category not found"));
        return CategoryMapper.toCategoryDto(current);
    }
}
