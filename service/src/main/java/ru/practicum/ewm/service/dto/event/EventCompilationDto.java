package ru.practicum.ewm.service.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@AllArgsConstructor
@Data
@ToString
@Builder
public class EventCompilationDto {
    private final Long eventId;
    private final Long compilationId;
}
