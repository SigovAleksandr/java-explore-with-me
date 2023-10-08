package ru.practicum.ewm.service.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.ewm.service.dto.event.EventShortDto;

import java.util.List;

@AllArgsConstructor
@Data
public class CompilationDto {
    private final Long id;
    private final Boolean pinned;
    private final String title;
    private final List<EventShortDto> events;
}
