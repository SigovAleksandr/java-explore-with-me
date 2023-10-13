package ru.practicum.ewm.service.service.interfaces;

import ru.practicum.ewm.service.dto.comment.CommentRequestDto;
import ru.practicum.ewm.service.dto.comment.CommentResponseDto;

import java.util.List;

public interface CommentService {
    CommentResponseDto addComment(Long userId, Long eventId, CommentRequestDto commentDto);

    CommentResponseDto updateComment(Long commentId, Long userId, CommentRequestDto commentDto);

    void deleteCommentById(Long commentId, Long userId);

    void deleteCommentByAdmin(Long commentId);


    List<CommentResponseDto> getAllCommentsByEventId(Long eventId, Integer from, Integer size);

    List<CommentResponseDto> getLast10CommentsByEventId(Long eventId);
}
