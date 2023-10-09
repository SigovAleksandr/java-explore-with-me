package ru.practicum.ewm.service.util;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@Slf4j
@Service
public class UtilityClass {

    public static final String USER_NOT_FOUND = "Пользователь не найден";
    public static final String CATEGORY_NOT_FOUND = "Категория не найдена";
    public static final String EVENT_NOT_FOUND = "Событие не найдено";
    public static final String COMPILATION_NOT_FOUND = "Компиляция не найдена";
    public static final String REQUEST_NOT_FOUND = "Запрос не найден";
    public static final String PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN);

    public static String formatTimeToString(LocalDateTime time) {
        return time.format(formatter);
    }

}
