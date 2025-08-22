package com.victory.evertalk.domain.character.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "characters")
public class Character {

    @Id
    @Column(name = "character_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer characterId;

    @Column(name = "character_name", length = 30)
    private String characterName;

    @Column(name = "personality")
    private String personality;

}
