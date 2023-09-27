package ru.practicum.ewm.stats.server;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.stats.dto.EndpointHitDto;
import ru.practicum.ewm.stats.dto.ViewStatsDto;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class StatsServerController {

    private final StatsServerService statsServerService;

    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public StatsServerController(StatsServerService statsServerService) {
        this.statsServerService = statsServerService;
    }

    @PostMapping(path = "/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void addEndpointHit(@RequestBody @Valid EndpointHitDto endpointHitDto) {
        statsServerService.addHit(endpointHitDto);
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> getStats(@RequestParam @DateTimeFormat(pattern = DATETIME_FORMAT) LocalDateTime start,
                                       @RequestParam @DateTimeFormat(pattern = DATETIME_FORMAT) LocalDateTime end,
                                       @RequestParam(required = false) List<String> uris,
                                       @RequestParam(defaultValue = "false") Boolean unique) {
        return statsServerService.getStats(start, end, uris, unique);
    }
}
