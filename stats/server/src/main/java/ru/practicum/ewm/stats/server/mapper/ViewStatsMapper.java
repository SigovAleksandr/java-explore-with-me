package ru.practicum.ewm.stats.server.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.stats.dto.ViewStatsDto;
import ru.practicum.ewm.stats.server.entity.EndpointHit;

@UtilityClass
public class ViewStatsMapper {

    public ViewStatsDto toViewStatsDto(EndpointHit endpointHit) {
        return ViewStatsDto.builder()
                .app(endpointHit.getApp())
                .uri(endpointHit.getUri())
                .build();
    }
}
