package ru.practicum.ewm.service.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@AllArgsConstructor
@Data
@ToString
@Builder
public class ConfirmedEventDto {
    private Long eventId;
    private Long count;
}
