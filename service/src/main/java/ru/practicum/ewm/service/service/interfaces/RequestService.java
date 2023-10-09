package ru.practicum.ewm.service.service.interfaces;

import ru.practicum.ewm.service.dto.request.EventRequestStatusUpdateRequestDto;
import ru.practicum.ewm.service.dto.request.EventRequestStatusUpdateResponseDto;
import ru.practicum.ewm.service.dto.request.RequestDto;

import java.util.List;

public interface RequestService {
    RequestDto addRequest(Long userId, Long eventId);

    RequestDto cancelRequest(Long userId, Long requestId);

    List<RequestDto> getAllUserRequests(Long userId);

    List<RequestDto> getAllUserEventRequests(Long eventId, Long userId);

    EventRequestStatusUpdateResponseDto updateRequestsStatus(EventRequestStatusUpdateRequestDto updater,
                                                             Long eventId, Long userId);
}
