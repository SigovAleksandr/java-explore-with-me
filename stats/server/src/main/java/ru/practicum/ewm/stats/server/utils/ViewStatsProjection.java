package ru.practicum.ewm.stats.server.utils;

public interface ViewStatsProjection {
    String getApp();

    String getUri();

    Long getHits();
}
