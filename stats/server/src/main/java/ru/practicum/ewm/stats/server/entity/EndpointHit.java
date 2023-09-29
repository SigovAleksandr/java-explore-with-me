package ru.practicum.ewm.stats.server.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hits")
public class EndpointHit {
    @Id
    @Column(name = "hit_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "hit_app")
    String app;

    @Column(name = "hit_uri")
    String uri;

    @Column(name = "hit_ip")
    String ip;

    @Column(name = "hit_timestamp")
    LocalDateTime timestamp;
}
