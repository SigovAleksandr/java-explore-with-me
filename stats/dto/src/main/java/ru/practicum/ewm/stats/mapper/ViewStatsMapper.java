package ru.practicum.ewm.stats.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.stats.dto.ViewStatsDto;
import ru.practicum.ewm.stats.model.EndpointHit;

@UtilityClass
public class ViewStatsMapper {

    public ViewStatsDto toViewStatsDto(EndpointHit endpointHit) {
        return ViewStatsDto.builder()
                .app(endpointHit.getApp())
                .uri(endpointHit.getUri())
                .build();
    }
}
