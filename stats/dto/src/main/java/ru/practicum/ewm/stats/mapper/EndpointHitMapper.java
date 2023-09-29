package ru.practicum.ewm.stats.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.stats.dto.EndpointHitDto;
import ru.practicum.ewm.stats.server.entity.EndpointHit;

@UtilityClass
public class EndpointHitMapper {

    public EndpointHitDto toEndpointHitDto(EndpointHit endpointHit) {
        return EndpointHitDto.builder()
                .app(endpointHit.getApp())
                .uri(endpointHit.getUri())
                .ip(endpointHit.getIp())
                .hitTimestamp(endpointHit.getTimestamp())
                .build();
    }

    public EndpointHit toEndpointHit(EndpointHitDto endpointHitDto) {
        return EndpointHit.builder()
                .app(endpointHitDto.getApp())
                .uri(endpointHitDto.getUri())
                .ip(endpointHitDto.getIp())
                .timestamp(endpointHitDto.getHitTimestamp())
                .build();
    }
}
