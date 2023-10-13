package ru.practicum.ewm.service.controller.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.service.service.interfaces.CommentService;

@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping("/admin/comments")
public class CommentAdminController {
    private final CommentService commentService;

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentByAdmin(@PathVariable Long commentId) {
        log.info("Удаление комментария администратором");
        commentService.deleteCommentByAdmin(commentId);
    }
}
