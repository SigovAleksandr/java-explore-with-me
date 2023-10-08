package ru.practicum.ewm.service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.service.model.enums.RequestStatus;

import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@Data
@Builder
public class EventRequestStatusUpdateRequestDto {
    @NotNull
    private final RequestStatus status;
    private final List<Long> requestIds;
}
