package ru.practicum.ewm.stats.server;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.stats.dto.EndpointHitDto;
import ru.practicum.ewm.stats.dto.ViewStatsDto;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class StatsServerController {

    private final StatsServerService statsServerService;

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public StatsServerController(StatsServerService statsServerService) {
        this.statsServerService = statsServerService;
    }

    @PostMapping(path = "/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void addEndpointHit(@RequestBody @Valid EndpointHitDto endpointHitDto) {
        statsServerService.addHit(endpointHitDto);
    }

    @GetMapping(path = "/stats")                                                  //Получение статистики за период
    public List<ViewStatsDto> getStats(@RequestParam String start,
                                         @RequestParam String end,
                                         @RequestParam(required = false) String[] uris,
                                         @RequestParam(defaultValue = "false") boolean unique) {
        List<ViewStatsDto> list = new ArrayList<>();
        if (unique) {                                                        //Получение статистики для уникального ip
            for (String uri : uris) {
                list.add(statsServerService.getUniqueIpStats(LocalDateTime.parse(start, FORMATTER),
                        LocalDateTime.parse(end, FORMATTER), uri));
            }
        } else {
            for (String uri : uris) {
                list.add(statsServerService.getUriStats(LocalDateTime.parse(start, FORMATTER),
                        LocalDateTime.parse(end, FORMATTER), uri));
            }
        }
        return list.stream()
                .sorted(Comparator.comparingLong(ViewStatsDto::getHits).reversed())
                .collect(Collectors.toList());
    }
}
