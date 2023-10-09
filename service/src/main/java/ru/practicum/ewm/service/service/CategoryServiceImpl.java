package ru.practicum.ewm.service.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.service.dto.category.CategoryDto;
import ru.practicum.ewm.service.dto.category.NewCategoryDto;
import ru.practicum.ewm.service.mapper.CategoryMapper;
import ru.practicum.ewm.service.model.Category;
import ru.practicum.ewm.service.repository.CategoryRepository;
import ru.practicum.ewm.service.service.interfaces.CategoryService;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.service.util.UtilityClass.CATEGORY_NOT_FOUND;

@AllArgsConstructor
@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {
        Category category = CategoryMapper.INSTANCE.toModel(newCategoryDto);
        return CategoryMapper.INSTANCE.toDto(categoryRepository.save(category));
    }

    @Transactional
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(CATEGORY_NOT_FOUND)
        );
        category = CategoryMapper.INSTANCE.forUpdate(categoryDto, category);
        return CategoryMapper.INSTANCE.toDto(category);
    }

    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException(CATEGORY_NOT_FOUND);
        }
        categoryRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(CATEGORY_NOT_FOUND)
        );
        return CategoryMapper.INSTANCE.toDto(category);
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> getAllCategories(Integer from, Integer size) {
        List<Category> categories = categoryRepository.findAll(PageRequest.of(from, size)).getContent();
        return categories.stream().map(CategoryMapper.INSTANCE::toDto).collect(Collectors.toList());
    }
}
