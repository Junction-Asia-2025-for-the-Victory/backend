package com.victory.evertalk.domain.episode.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "episode")
public class Episode {

    @Id
    @Column(name = "episode_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer episodeId;

    @Column(name = "title")
    private String title;

    @Column(name = "background", columnDefinition = "TEXT")
    private String background;

    @Column(name = "start")
    private String start;

    @Column(name = "end")
    private String end;


}
