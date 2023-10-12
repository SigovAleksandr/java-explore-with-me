package ru.practicum.ewm.service.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.service.model.Comment;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Integer countById(Long commentId);

    List<Comment> findAllByEventIdOrderByCreatedOnDesc(Long eventId, PageRequest of);

    Optional<Comment> findByEventIdAndAuthorId(Long eventId, Long userId);

    List<Comment> findTop10ByEventIdOrderByCreatedOnDesc(Long eventId);

    List<Comment> findAllByEventIdOrderByCreatedOnDesc(Long eventId);
}
