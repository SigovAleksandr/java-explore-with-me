package ru.practicum.ewm.service.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@AllArgsConstructor
@Data
@Builder
@ToString
public class UserShortDto {
    private final String name;
    private final String email;
}
