package ru.practicum.ewm.service.service.interfaces;

import ru.practicum.ewm.service.dto.compilation.CompilationDto;
import ru.practicum.ewm.service.dto.compilation.CompilationUpdateDto;
import ru.practicum.ewm.service.dto.compilation.NewCompilationDto;

import java.util.List;

public interface CompilationService {
    CompilationDto addCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilation(Long compId);

    CompilationDto updateCompilation(Long compId, CompilationUpdateDto updateCompilationDto);

    CompilationDto getCompilationById(Long compId);

    List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);
}