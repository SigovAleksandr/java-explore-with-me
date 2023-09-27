package ru.practicum.ewm.stats.server;

import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.dto.EndpointHitDto;
import ru.practicum.ewm.stats.dto.ViewStatsDto;
import ru.practicum.ewm.stats.mapper.EndpointHitMapper;
import ru.practicum.ewm.stats.mapper.ViewStatsMapper;
import ru.practicum.ewm.stats.model.EndpointHit;
import ru.practicum.ewm.stats.server.exception.ResourceNotFoundException;

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
    public ViewStatsDto getUriStats(LocalDateTime start, LocalDateTime end, String uri) {
        List<EndpointHit> list = statsServerRepository.findAllByTimestampBetweenAndUri(start, end, uri);
        return addHitCount(list);
    }

    @Override
    public ViewStatsDto getUniqueIpStats(LocalDateTime start, LocalDateTime end, String uri) {
        List<EndpointHit> list = statsServerRepository.findUniqueUriStats(uri, start, end);
        return addHitCount(list);
    }

    private ViewStatsDto addHitCount(List<EndpointHit> list) {
        long hits = list.size();
        if (list.isEmpty()) {
            throw new ResourceNotFoundException("Hits not found");
        }
        ViewStatsDto dto = ViewStatsMapper.toViewStatsDto(list.get(0));
        dto.setHits(hits);
        return dto;
    }
}
