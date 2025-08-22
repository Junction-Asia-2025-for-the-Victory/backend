package com.victory.evertalk.domain.episode.repository;

import com.victory.evertalk.domain.episode.entity.Episode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EpisodeRepository extends JpaRepository<Episode, Integer> {

    @Query("SELECT DISTINCT c.episode " +
            "FROM Chat c " +
            "WHERE c.user.userId = :userId " +
            "AND c.character.characterId = :characterId")
    List<Episode> findAllEpisodesByUserIdAndCharacterId(@Param("userId") Integer userId,
                                                        @Param("characterId") Integer characterId);

}
