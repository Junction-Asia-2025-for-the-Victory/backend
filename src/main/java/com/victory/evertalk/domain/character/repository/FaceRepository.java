package com.victory.evertalk.domain.character.repository;

import com.victory.evertalk.domain.character.entity.Face;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FaceRepository extends JpaRepository<Face, Integer> {

    Optional<Face> findFaceByCharacter_CharacterIdAndEmotion_EmotionId(Integer characterId, Integer emotionId);

}
