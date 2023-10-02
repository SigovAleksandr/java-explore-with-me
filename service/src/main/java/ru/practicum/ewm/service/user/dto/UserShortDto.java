package ru.practicum.ewm.service.user.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserShortDto {
    private Long id;
    private String name;
}
