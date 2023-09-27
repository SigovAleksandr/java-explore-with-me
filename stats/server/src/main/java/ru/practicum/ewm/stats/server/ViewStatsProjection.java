package ru.practicum.ewm.stats.server;

public interface ViewStatsProjection {
    String getApp();
    String getUri();
    Long getHits();
}
