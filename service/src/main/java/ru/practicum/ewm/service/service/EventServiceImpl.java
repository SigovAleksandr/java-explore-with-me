package ru.practicum.ewm.service.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.client.StatsClient;
import ru.practicum.ewm.service.dto.comment.CommentResponseDto;
import ru.practicum.ewm.service.dto.event.*;
import ru.practicum.ewm.service.exception.DataException;
import ru.practicum.ewm.service.mapper.CommentMapper;
import ru.practicum.ewm.service.mapper.EventMapper;
import ru.practicum.ewm.service.mapper.LocationMapper;
import ru.practicum.ewm.service.model.*;
import ru.practicum.ewm.service.model.enums.EventState;
import ru.practicum.ewm.service.model.enums.RequestStatus;
import ru.practicum.ewm.service.model.enums.StateAction;
import ru.practicum.ewm.service.repository.*;
import ru.practicum.ewm.service.service.interfaces.EventService;
import ru.practicum.ewm.stats.dto.EndpointHitDto;
import ru.practicum.ewm.stats.dto.ViewStatsDto;

import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.ewm.service.util.UtilityClass.*;

@AllArgsConstructor
@Slf4j
@Service
public class EventServiceImpl implements EventService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final LocationRepository locationRepository;
    private final CommentRepository commentRepository;
    private final StatsClient statClient = new StatsClient();

    private static final String START = "1970-01-01 00:00:00";
    private static final String APP = "ewm-main-service";

    @Transactional
    public EventDto addEvent(Long userId, NewEventDto newEventDto) {
        LocalDateTime eventDate = newEventDto.getEventDate();
        LocalDateTime timeCriteria = LocalDateTime.now().plusHours(2L);
        if (eventDate.isBefore(timeCriteria)) {
            throw new DataException("Время события не может таким ранним");
        }

        User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(USER_NOT_FOUND)
        );
        long categoryId = newEventDto.getCategory();
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new EntityNotFoundException(CATEGORY_NOT_FOUND)
        );
        Location location = LocationMapper.INSTANCE.toModel(newEventDto.getLocation());
        location = locationRepository.save(location);
        Event event = EventMapper.INSTANCE.toModel(newEventDto, user, category);
        event.setState(EventState.PENDING);
        event.setCreatedOn(LocalDateTime.now());
        event.setLocation(location);
        List<CommentResponseDto> comments = new ArrayList<>();
        return EventMapper.INSTANCE.toDto(eventRepository.save(event), 0L, 0L, comments);
    }

    @Transactional
    public EventDto updateEventByAdmin(Long eventId, EventUpdateRequestDto updater) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new EntityNotFoundException(EVENT_NOT_FOUND)
        );
        if (event.getState() == EventState.PUBLISHED) {
            throw new DataException("Событие уже опубликовано");
        }
        LocalDateTime time = LocalDateTime.now().plusHours(1);
        Long newCategoryId = updater.getCategory();
        Category oldCategory = event.getCategory();
        Category newCategory = oldCategory;
        if (newCategoryId != null) {
            if (oldCategory == null || !oldCategory.getId().equals(newCategoryId)) {
                newCategory = categoryRepository.findById(newCategoryId).orElseThrow(
                        () -> new EntityNotFoundException(CATEGORY_NOT_FOUND)
                );
            }
        }

        EventState newState = event.getState();
        StateAction action = updater.getStateAction();
        if (action != null) {
            if (event.getState() != EventState.PENDING) {
                throw new DataException("Неверный статус события");
            } else if (
                    event.getEventDate().isBefore(time)
                            && action == StateAction.PUBLISH_EVENT
            ) {
                throw new DataException("Уже поздно публиковать событие");
            }
            switch (action) {
                case PUBLISH_EVENT:
                    newState = EventState.PUBLISHED;
                    event.setPublishedOn(LocalDateTime.now());
                    break;
                case REJECT_EVENT:
                    newState = EventState.CANCELED;
                    break;
                default:
                    throw new IllegalArgumentException("Неверный статус");
            }
            event.setState(newState);
        }
        event = EventMapper.INSTANCE.forUpdate(updater, newCategory, newState, event);
        List<CommentResponseDto> comments = CommentMapper.INSTANCE.toDtos(commentRepository.findAllByEventIdOrderByCreatedOnDesc(eventId));
        return EventMapper.INSTANCE.toDto(event, 0L, 0L, comments);
    }

    @Transactional
    public EventDto updateEventByUser(EventUpdateRequestDto updateEventDto, Long eventId, Long userId) {
        LocalDateTime eventDate = updateEventDto.getEventDate();
        LocalDateTime timeCriteria = LocalDateTime.now().plusHours(2L);
        if (eventDate != null && eventDate.isBefore(timeCriteria)) {
            throw new DataException("Время события не может таким ранним");
        }
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new EntityNotFoundException(EVENT_NOT_FOUND)
        );
        Long newCategoryId = updateEventDto.getCategory();
        Category oldCategory = event.getCategory();
        Category newCategory = oldCategory;
        if (newCategoryId != null) {
            if (oldCategory == null || !oldCategory.getId().equals(newCategoryId)) {
                newCategory = categoryRepository.findById(newCategoryId).orElseThrow(
                        () -> new EntityNotFoundException(CATEGORY_NOT_FOUND)
                );
            }
        }

        User initiator = event.getInitiator();
        if (!Objects.equals(initiator.getId(), userId)) {
            throw new DataException("Пользователь не является автором события");
        }
        if (event.getState() == EventState.PUBLISHED) {
            throw new DataException("Неверный статус события");
        }
        EventState newState = event.getState();
        StateAction action = updateEventDto.getStateAction();
        if (action != null) {
            switch (action) {
                case SEND_TO_REVIEW:
                    newState = EventState.PENDING;
                    break;
                case CANCEL_REVIEW:
                    newState = EventState.CANCELED;
                    break;
                default:
                    throw new IllegalArgumentException("Неверный статус");
            }
        }
        event = EventMapper.INSTANCE.forUpdate(updateEventDto, newCategory, newState, event);
        List<CommentResponseDto> comments = CommentMapper.INSTANCE.toDtos(commentRepository.findAllByEventIdOrderByCreatedOnDesc(eventId));
        return EventMapper.INSTANCE.toDto(event, 0L, 0L, comments);
    }

    @Transactional(readOnly = true)
    public EventDto getEventByIdByInitiator(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new EntityNotFoundException(EVENT_NOT_FOUND)
        );
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException(USER_NOT_FOUND);
        }
        User initiator = event.getInitiator();
        if (!initiator.getId().equals(userId)) {
            throw new IllegalArgumentException("Пользователь не является автором события");
        }
        Long confirmedRequests = requestRepository.countByEventIdAndStatus(event.getId(), RequestStatus.CONFIRMED);
        Long views = getViewsForOneEvent(eventId);
        List<CommentResponseDto> comments = CommentMapper.INSTANCE.toDtos(commentRepository.findAllByEventIdOrderByCreatedOnDesc(eventId));
        return EventMapper.INSTANCE.toDto(event, confirmedRequests, views, comments);
    }

    @Transactional(readOnly = true)
    public EventDto getEventByIdPublic(Long eventId, String ip, String uri) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new EntityNotFoundException(EVENT_NOT_FOUND)
        );
        if (event.getState() != EventState.PUBLISHED) {
            throw new EntityNotFoundException("Событие не опубликовано");
        }
        Long confirmedRequests = requestRepository.countByEventIdAndStatus(event.getId(), RequestStatus.CONFIRMED);
        statClient.addHit(new EndpointHitDto(
                APP,
                uri,
                ip,
                LocalDateTime.now()
        ));
        Long views = getViewsForOneEvent(eventId);
        List<CommentResponseDto> comments = CommentMapper.INSTANCE.toDtos(commentRepository.findAllByEventIdOrderByCreatedOnDesc(eventId));
        return EventMapper.INSTANCE.toDto(event, confirmedRequests, views, comments);
    }

    @Transactional(readOnly = true)
    public List<EventShortDto> getEventsPublic(
            String text,
            List<Long> categoryIds,
            Boolean paid,
            LocalDateTime start,
            LocalDateTime end,
            Boolean onlyAvailable,
            String sort,
            String ip,
            String uri,
            Integer from,
            Integer size
    ) {
        statClient.addHit(new EndpointHitDto(
                APP,
                uri,
                ip,
                LocalDateTime.now()
        ));
        if (start == null) {
            start = LocalDateTime.now();
        }
        if (end == null) {
            end = LocalDateTime.now().plusYears(10000);
        }
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Некорректный период времени");
        }
        Specification<Event> spec = Specification.where(inStates(List.of(EventState.PUBLISHED)))
                .and(inEventDates(start, end))
                .and(inCategoryIds(categoryIds))
                .and(byPaid(paid))
                .and(byTextInAnnotationOrDescription(text));

        if (onlyAvailable != null && onlyAvailable) {
            spec = spec.and(byParticipantLimit());
        }
        PageRequest pageRequest = PageRequest.of(
                from / size,
                size,
                Sort.by(Sort.Direction.DESC, "eventDate"));
        List<Event> events = eventRepository.findAll(spec, pageRequest).getContent();
        if (events.isEmpty()) {
            return new ArrayList<>();
        }
        List<EventShortDto> eventShortDtos = makeEventShortDto(events);
        if (Objects.equals(sort, "VIEWS")) {
            eventShortDtos = eventShortDtos.stream()
                    .sorted(Comparator.comparing(EventShortDto::getViews).reversed())
                    .collect(Collectors.toList());
        }
        return eventShortDtos;
    }

    @Transactional(readOnly = true)
    public List<EventShortDto> getEventsInitiatedByUser(Long userId, Integer from,
                                                        Integer size) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException(USER_NOT_FOUND);
        }
        PageRequest pageRequest = PageRequest.of(from / size, size);
        List<Event> events = eventRepository.findAllByInitiatorId(userId, pageRequest);
        if (events.isEmpty()) {
            return new ArrayList<>();
        }

        return makeEventShortDto(events);
    }

    @Transactional(readOnly = true)
    public List<EventDto> getAllEventsByAdmin(List<Long> users, List<Long> categories, List<EventState> states,
                                              LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {

        if (rangeStart == null) {
            rangeStart = LocalDateTime.now().minusYears(4000);
        }

        if (rangeEnd == null) {
            rangeEnd = LocalDateTime.now().plusYears(10000);
        }
        Specification<Event> eventSpecification = Specification.where(inEventDates(rangeStart, rangeEnd))
                .and(inCategoryIds(categories))
                .and(inStates(states))
                .and(inUserIds(users));

        PageRequest pageRequest = PageRequest.of(
                from / size,
                size,
                Sort.by(Sort.Direction.DESC, "eventDate"));

        List<Event> events = eventRepository.findAll(eventSpecification, pageRequest).getContent();
        if (events.isEmpty()) {
            return new ArrayList<>();
        }
        return makeEventDtos(events);
    }

    private Long getViewsForOneEvent(Long eventId) {
        List<String> urisToSend = List.of(String.format("/events/%s", eventId));
        List<ViewStatsDto> viewStats = statClient.getStats(
                START,
                formatTimeToString(LocalDateTime.now()),
                urisToSend,
                true
        );

        ViewStatsDto viewStatsDto = viewStats == null || viewStats.isEmpty() ? null : viewStats.get(0);
        return viewStatsDto == null || viewStatsDto.getHits() == null ? 0 : viewStatsDto.getHits();
    }

    private List<EventDto> makeEventDtos(List<Event> events) {
        Map<String, Long> viewStatsMap = toViewStats(events);

        Map<Long, Long> confirmedRequests = getConfirmedRequests(events);

        List<EventDto> eventsDto = new ArrayList<>();
        for (Event event : events) {
            Long eventId = event.getId();
            Long reqCount = confirmedRequests.get(eventId);
            Long views = viewStatsMap.get(String.format("/events/%s", eventId));
            if (reqCount == null) {
                reqCount = 0L;
            }
            if (views == null) {
                views = 0L;
            }
            List<CommentResponseDto> comments = CommentMapper.INSTANCE.toDtos(commentRepository.findAllByEventIdOrderByCreatedOnDesc(eventId));
            eventsDto.add(
                    EventMapper.INSTANCE.toDto(event, reqCount, views, comments)
            );
        }

        return eventsDto;
    }

    private Specification<Event> inUserIds(List<Long> users) {
        return users == null ? null : (root, query, criteriaBuilder) ->
                criteriaBuilder.in(root.get("initiator").get("id")).value(users);
    }

    private Specification<Event> inCategoryIds(List<Long> categories) {
        return categories == null ? null : (root, query, criteriaBuilder) ->
                criteriaBuilder.in(root.get("category").get("id")).value(categories);
    }

    private Specification<Event> inStates(List<EventState> states) {
        return states == null ? null : (root, query, criteriaBuilder) ->
                criteriaBuilder.in(root.get("state")).value(states);
    }

    private Specification<Event> inEventDates(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        return rangeStart == null || rangeEnd == null ? null : (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("eventDate"), rangeStart, rangeEnd);
    }

    private Specification<Event> byPaid(Boolean paid) {
        return paid == null ? null : (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("paid"), paid);
    }

    private Specification<Event> byTextInAnnotationOrDescription(String text) {
        return text == null ? null : (root, query, criteriaBuilder) -> {
            String lowerCasedText = text.toLowerCase();
            Expression<String> annotation = criteriaBuilder.lower(root.get("annotation"));
            Expression<String> description = criteriaBuilder.lower(root.get("description"));
            return criteriaBuilder.or(
                    criteriaBuilder.like(annotation, "%" + lowerCasedText + "%"),
                    criteriaBuilder.like(description, "%" + lowerCasedText + "%")
            );
        };
    }

    private Specification<Event> byParticipantLimit() {
        return (root, query, criteriaBuilder) -> {
            Subquery<Long> sub = query.subquery(Long.class);
            Root<Request> subRoot = sub.from(Request.class);
            sub.select(criteriaBuilder.count(subRoot.get("id"))).where(
                    criteriaBuilder.and(
                            criteriaBuilder.equal(subRoot.get("status"), RequestStatus.CONFIRMED),
                            criteriaBuilder.equal(subRoot.get("event").get("id"), root.get("id"))
                    )
            );
            return criteriaBuilder.greaterThan(root.get("participantLimit"), sub);
        };
    }

    private Map<Long, Long> getConfirmedRequests(Collection<Event> events) {
        List<Long> eventsIds = events.stream()
                .map(Event::getId)
                .collect(Collectors.toList());
        List<ConfirmedEventDto> confirmedDtos =
                requestRepository.countConfirmedRequests(eventsIds, RequestStatus.CONFIRMED);
        return confirmedDtos.stream()
                .collect(Collectors.toMap(ConfirmedEventDto::getEventId, ConfirmedEventDto::getCount));
    }

    private Map<String, Long> toViewStats(Collection<Event> events) {
        List<String> urisToSend = new ArrayList<>();
        for (Event event : events) {
            urisToSend.add(String.format("/events/%s", event.getId()));
        }
        List<ViewStatsDto> viewStats = statClient.getStats(
                START,
                formatTimeToString(LocalDateTime.now()),
                urisToSend,
                true
        );

        return viewStats.stream()
                .collect(Collectors.toMap(ViewStatsDto::getUri, ViewStatsDto::getHits));
    }

    public List<EventShortDto> makeEventShortDto(Collection<Event> events) {
        Map<String, Long> viewStatsMap = toViewStats(events);

        Map<Long, Long> confirmedRequests = getConfirmedRequests(events);

        List<EventShortDto> eventsDto = new ArrayList<>();
        for (Event event : events) {
            Long eventId = event.getId();
            Long reqCount = confirmedRequests.get(eventId);
            Long views = viewStatsMap.get(String.format("/events/%s", eventId));
            if (reqCount == null) {
                reqCount = 0L;
            }
            if (views == null) {
                views = 0L;
            }
            List<CommentResponseDto> comments = CommentMapper.INSTANCE.toDtos(commentRepository.findAllByEventIdOrderByCreatedOnDesc(eventId));
            eventsDto.add(
                    EventMapper.INSTANCE.toShortDto(event, reqCount, views, comments)
            );
        }
        return eventsDto;
    }
}
