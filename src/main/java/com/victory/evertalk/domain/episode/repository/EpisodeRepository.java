package com.victory.evertalk.domain.episode.repository;

import com.victory.evertalk.domain.episode.entity.Episode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EpisodeRepository extends JpaRepository<Episode, Integer> {

        @Query("""
        SELECT DISTINCT e
        FROM Episode e
        LEFT JOIN Chat c
          ON c.episode = e
         AND c.user.userId = :userId
         AND c.character.characterId = :characterId
        ORDER BY e.episodeId
        """)
        List<Episode> findAllEpisodesByUserIdAndCharacterId(@Param("userId") Integer userId,
                                                            @Param("characterId") Integer characterId);
    }


