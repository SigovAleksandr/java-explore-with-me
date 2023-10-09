package ru.practicum.ewm.service.controller.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.service.dto.event.EventDto;
import ru.practicum.ewm.service.dto.event.EventUpdateRequestDto;
import ru.practicum.ewm.service.model.enums.EventState;
import ru.practicum.ewm.service.service.interfaces.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping("/admin/events")
public class EventAdminController {

    private final EventService eventService;

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventDto updateEvent(
            @PathVariable Long eventId,
            @Valid @RequestBody EventUpdateRequestDto requestDto
    ) {
        log.info("Обновление события с: {}, на: {}", eventId, requestDto);
        return eventService.updateEventByAdmin(eventId, requestDto);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<EventDto> getAllEvents(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) List<EventState> states,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(name = "size", defaultValue = "10") @Positive Integer size
    ) {
        log.info("Вызов всех событий");
        return eventService.getAllEventsByAdmin(users, categories, states, rangeStart, rangeEnd, from, size);
    }

}
