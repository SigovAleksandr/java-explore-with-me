package ru.practicum.ewm.service.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import ru.practicum.ewm.service.dto.category.CategoryDto;
import ru.practicum.ewm.service.dto.category.NewCategoryDto;
import ru.practicum.ewm.service.model.Category;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    @Mapping(target = "id", ignore = true)
    Category toModel(NewCategoryDto newCategoryDto);

    CategoryDto toDto(Category category);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    Category forUpdate(CategoryDto categoryDto, @MappingTarget Category category);
}