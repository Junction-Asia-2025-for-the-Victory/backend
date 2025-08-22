package com.victory.evertalk.domain.character.entity;

import com.victory.evertalk.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "likeability")
public class Likeability {

    @Id
    @Column(name = "likeability_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer likeabilityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", nullable = false)
    private Character character;

    @Column(name = "count")
    private Integer count;

}
