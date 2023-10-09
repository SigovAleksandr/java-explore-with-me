package ru.practicum.ewm.service.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import ru.practicum.ewm.service.dto.event.EventDto;
import ru.practicum.ewm.service.dto.event.EventShortDto;
import ru.practicum.ewm.service.dto.event.EventUpdateRequestDto;
import ru.practicum.ewm.service.dto.event.NewEventDto;
import ru.practicum.ewm.service.model.Category;
import ru.practicum.ewm.service.model.Event;
import ru.practicum.ewm.service.model.User;
import ru.practicum.ewm.service.model.enums.EventState;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {
    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "compilations", ignore = true)
    @Mapping(target = "category", source = "category")
    @Mapping(target = "initiator", source = "user")
    Event toModel(NewEventDto newEventDto, User user, Category category);

    @Mapping(target = "confirmedRequests", source = "confirmedRequests")
    @Mapping(target = "views", source = "views")
    EventDto toDto(Event event, Long confirmedRequests, Long views);

    @Mapping(target = "confirmedRequests", source = "confirmedRequests")
    @Mapping(target = "views", source = "views")
    EventShortDto toShortDto(Event event, Long confirmedRequests, Long views);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "compilations", ignore = true)
    @Mapping(target = "category", source = "newCategory")
    @Mapping(target = "initiator", ignore = true)
    @Mapping(target = "state", source = "newState")
    Event forUpdate(
            EventUpdateRequestDto updateEventDto,
            Category newCategory,
            EventState newState,
            @MappingTarget Event event
    );
}