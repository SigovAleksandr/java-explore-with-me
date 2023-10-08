package ru.practicum.ewm.stats.server.service;

import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.dto.EndpointHitDto;
import ru.practicum.ewm.stats.dto.ViewStatsDto;
import ru.practicum.ewm.stats.server.mapper.EndpointHitMapper;
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
        if (start == null || end == null) {
            throw new IllegalArgumentException("Dates of start and end of period must be specified");
        }
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start date must be before end date");
        }
        if (unique) {
            if (uris == null || uris.isEmpty()) {
                return statsServerRepository.getAllStatsByDistinctIp(start, end);
            } else {
                return statsServerRepository.getAllStatsInUrisByDistinctIp(uris, start, end);
            }
        } else {
            if (uris == null || uris.isEmpty()) {
                return statsServerRepository.getAllStats(start, end);
            } else {
                return statsServerRepository.getAllStatsInUris(uris, start, end);
            }
        }
    }
}
