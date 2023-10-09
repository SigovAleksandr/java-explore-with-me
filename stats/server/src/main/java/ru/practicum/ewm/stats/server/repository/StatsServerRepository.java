package ru.practicum.ewm.stats.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.stats.dto.ViewStatsDto;
import ru.practicum.ewm.stats.server.entity.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsServerRepository extends JpaRepository<EndpointHit, Long> {
    @Query("select new ru.practicum.ewm.stats.dto.ViewStatsDto(e.app, e.uri, count(e.ip)) " +
            "from EndpointHit e " +
            "where e.timestamp between :start and :end " +
            "group by e.app, e.uri " +
            "order by count(e.ip) desc")
    List<ViewStatsDto> getAllStats(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("select new ru.practicum.ewm.stats.dto.ViewStatsDto(e.app, e.uri, count(e.ip)) " +
            "from EndpointHit e " +
            "where e.uri in :uris and e.timestamp between :start and :end " +
            "group by e.app, e.uri " +
            "order by count(e.ip) desc")
    List<ViewStatsDto> getAllStatsInUris(@Param("uris") List<String> uris, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("select new ru.practicum.ewm.stats.dto.ViewStatsDto(e.app, e.uri, count(distinct e.ip)) " +
            "from EndpointHit e " +
            "where e.uri in :uris and e.timestamp between :start and :end " +
            "group by e.app, e.uri " +
            "order by count(distinct e.ip) desc")
    List<ViewStatsDto> getAllStatsInUrisByDistinctIp(@Param("uris") List<String> uris, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("select new ru.practicum.ewm.stats.dto.ViewStatsDto(e.app, e.uri, count(distinct e.ip)) " +
            "from EndpointHit e " +
            "where e.timestamp between :start and :end " +
            "group by e.app, e.uri " +
            "order by count(distinct e.ip) desc")
    List<ViewStatsDto> getAllStatsByDistinctIp(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
