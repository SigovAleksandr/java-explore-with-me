package ru.practicum.ewm.service.event.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LocationDto {
    @NotNull
    private Float lat;

    @NotNull
    private Float lon;
}
