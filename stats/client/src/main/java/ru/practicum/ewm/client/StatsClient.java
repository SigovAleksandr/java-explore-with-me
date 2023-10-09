package ru.practicum.ewm.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.ewm.stats.dto.EndpointHitDto;
import ru.practicum.ewm.stats.dto.ViewStatsDto;

import java.util.List;

public class StatsClient {

    private static final String STAT_SERVER_URL = System.getenv().get("STATS_SERVER_URL");
    private final RestTemplate restTemplate = new RestTemplate();

    public void addHit(EndpointHitDto endpointHitDto) {
        restTemplate.postForLocation(STAT_SERVER_URL + "/hit", endpointHitDto);
    }

    public List<ViewStatsDto> getStats(String start, String end, List<String> uris, boolean unique) {
        StringBuilder urisToSend = new StringBuilder();
        for (String uri : uris) {
            urisToSend.append(uri).append(",");
        }
        ResponseEntity<List<ViewStatsDto>> response = restTemplate.exchange(
                STAT_SERVER_URL + "/stats?start={start}&end={end}&uris={uris}&unique={unique}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                },
                start, end, urisToSend.toString(), unique);

        return response.getBody();
    }
}
