package ru.practicum.ewm.service.event.service;

import ru.practicum.ewm.service.event.dto.*;
import ru.practicum.ewm.service.event.entity.EventState;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    List<EventShortDto> getAllByUser(Long id, int from, int size);

    EventFullDto createEvent(NewEventDto newEventDto);

    EventFullDto getEventByIdAndUserId(Long eventId, Long userId);

    EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventUserRequestDto dto);

    List<EventFullDto> getAllByAdmin(List<Long> users,
                                     List<EventState> states,
                                     List<Long> categories,
                                     LocalDateTime rangeStart,
                                     LocalDateTime rangeEnd,
                                     int from,
                                     int size);

    EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequestDto dto);

    List<EventShortDto> getAllEvents(String text,
                                     List<Long> categories,
                                     Boolean paid,
                                     LocalDateTime rangeStart,
                                     LocalDateTime rangeEnd,
                                     boolean onlyAvailable,
                                     String sort,
                                     int from,
                                     int size);

    EventFullDto getEventById(Long eventId, HttpServletRequest request);
}
