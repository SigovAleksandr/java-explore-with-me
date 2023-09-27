package ru.practicum.ewm.stats.server;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.stats.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsServerRepository extends JpaRepository<EndpointHit, Long> {
    List<EndpointHit> findAllByTimestampBetweenAndUri(LocalDateTime start, LocalDateTime end, String uri);

    @Query(value = "SELECT DISTINCT ON (ip) hit_id, app, uri, ip, timestamp FROM hits WHERE uri=? " +
            "AND (timestamp between ? and ?)", nativeQuery = true)
    List<EndpointHit> findUniqueUriStats(String uri, LocalDateTime start, LocalDateTime end);
}
