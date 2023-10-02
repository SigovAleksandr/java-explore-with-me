package ru.practicum.ewm.service.event.service;

import org.springframework.stereotype.Service;
import ru.practicum.ewm.service.event.dto.*;
import ru.practicum.ewm.service.event.entity.EventState;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {
    @Override
    public List<EventShortDto> getAllByUser(Long id, int from, int size) {
        return null;
    }

    @Override
    public EventFullDto createEvent(NewEventDto newEventDto) {
        return null;
    }

    @Override
    public EventFullDto getEventByIdAndUserId(Long eventId, Long userId) {
        return null;
    }

    @Override
    public EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventUserRequestDto dto) {
        return null;
    }

    @Override
    public List<EventFullDto> getAllByAdmin(List<Long> users, List<EventState> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        return null;
    }

    @Override
    public EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequestDto dto) {
        return null;
    }

    @Override
    public List<EventShortDto> getAllEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, boolean onlyAvailable, String sort, int from, int size) {
        return null;
    }

    @Override
    public EventFullDto getEventById(Long eventId, HttpServletRequest request) {
        return null;
    }
}
