package ru.practicum.ewm.service.controller.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.service.dto.compilation.CompilationDto;
import ru.practicum.ewm.service.dto.compilation.CompilationUpdateDto;
import ru.practicum.ewm.service.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.service.service.interfaces.CompilationService;

import javax.validation.Valid;

@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping("/admin/compilations")
public class CompilationAdminController {

    private final CompilationService compilationService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        log.info("Создание компиляции: {}", newCompilationDto);
        return compilationService.addCompilation(newCompilationDto);
    }

    @PatchMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto updateCompilation(
            @PathVariable Long compId,
            @Valid @RequestBody CompilationUpdateDto updateCompilationDto
    ) {
        log.info("Обновление компиляции с: {}, на: {}", compId, updateCompilationDto);
        return compilationService.updateCompilation(compId, updateCompilationDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        log.info("Удаление компиляции: {}", compId);
        compilationService.deleteCompilation(compId);
    }

}
