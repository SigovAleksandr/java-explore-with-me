package ru.practicum.ewm.stats.server;

import ru.practicum.ewm.stats.dto.EndpointHitDto;
import ru.practicum.ewm.stats.dto.ViewStatsDto;

import java.time.LocalDateTime;

public interface StatsServerService {

    void addHit(EndpointHitDto endpointHitDto);

    ViewStatsDto getUriStats(LocalDateTime start, LocalDateTime end, String uri);

    ViewStatsDto getUniqueIpStats(LocalDateTime start, LocalDateTime end, String uri);
}
