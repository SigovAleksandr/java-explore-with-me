package ru.practicum.ewm.service.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import ru.practicum.ewm.service.dto.compilation.CompilationDto;
import ru.practicum.ewm.service.dto.compilation.CompilationUpdateDto;
import ru.practicum.ewm.service.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.service.dto.event.EventShortDto;
import ru.practicum.ewm.service.model.Compilation;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CompilationMapper {
    CompilationMapper INSTANCE = Mappers.getMapper(CompilationMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "events", ignore = true)
    Compilation toModel(NewCompilationDto newCompilationDto);

    @Mapping(target = "events", source = "events")
    CompilationDto toDto(Compilation compilation, List<EventShortDto> events);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "events", ignore = true)
    Compilation forUpdate(CompilationUpdateDto updateCompilationDto, @MappingTarget Compilation compilation);
}