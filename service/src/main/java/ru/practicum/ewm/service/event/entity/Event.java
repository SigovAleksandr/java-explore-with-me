package ru.practicum.ewm.service.event.entity;

import lombok.*;
import ru.practicum.ewm.service.category.entity.Category;
import ru.practicum.ewm.service.user.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "initiator_user_id")
    private User initiator;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @Column(name = "title")
    private String title;

    @Column(name = "annotation")
    private String annotation;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private EventState state;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "created")
    private LocalDateTime created;

    @Column(name = "published")
    private LocalDateTime published;

    @Column(name = "participant_limit")
    private Integer participantLimit;

    @Column(name = "paid")
    private Boolean isPaid;

    @Column(name = "request_moderation")
    private Boolean isRequestModeration;
}
