package ru.practicum.ewm.stats.server.service;

import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.dto.EndpointHitDto;
import ru.practicum.ewm.stats.dto.ViewStatsDto;
import ru.practicum.ewm.stats.mapper.EndpointHitMapper;
import ru.practicum.ewm.stats.server.repository.StatsServerRepository;
import ru.practicum.ewm.stats.server.utils.ViewStatsProjection;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatsServerServiceImpl implements StatsServerService {

    private final StatsServerRepository statsServerRepository;

    public StatsServerServiceImpl(StatsServerRepository statsServerRepository) {
        this.statsServerRepository = statsServerRepository;
    }

    @Override
    public void addHit(EndpointHitDto endpointHitDto) {
        statsServerRepository.save(EndpointHitMapper.toEndpointHit(endpointHitDto));
    }

    @Override
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        List<ViewStatsProjection> results;
        if (unique) {
            results = statsServerRepository.findUniqueStats(start, end, uris);
        } else {
            results = statsServerRepository.findNonUniqueStats(start, end, uris);
        }
        return results.stream()
                .map(result -> ViewStatsDto.builder()
                        .app(result.getApp())
                        .uri(result.getUri())
                        .hits(result.getHits())
                        .build())
                .collect(Collectors.toList());
    }
}
