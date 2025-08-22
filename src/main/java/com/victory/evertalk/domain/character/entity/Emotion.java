package com.victory.evertalk.domain.character.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "emotion")
public class Emotion {

    @Id
    @Column(name = "emotion_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer emotionId;

    @Column(name = "emotion")
    private String emotion;

}
