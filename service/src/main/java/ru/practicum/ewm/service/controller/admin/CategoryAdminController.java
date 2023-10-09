package ru.practicum.ewm.service.controller.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.service.dto.category.CategoryDto;
import ru.practicum.ewm.service.dto.category.NewCategoryDto;
import ru.practicum.ewm.service.service.interfaces.CategoryService;

import javax.validation.Valid;

@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping("/admin/categories")
public class CategoryAdminController {

    private final CategoryService categoryService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addCategory(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.info("Создание категории: {}", newCategoryDto);
        return categoryService.addCategory(newCategoryDto);
    }

    @PatchMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto updateCategory(
            @PathVariable
            Long catId,
            @Valid @RequestBody
            CategoryDto categoryDto
    ) {
        log.info("Обновление категории: {}, to: {}", catId, categoryDto);
        return categoryService.updateCategory(catId, categoryDto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long catId) {
        log.info("Удаление категории: {}", catId);
        categoryService.deleteCategory(catId);
    }

}
