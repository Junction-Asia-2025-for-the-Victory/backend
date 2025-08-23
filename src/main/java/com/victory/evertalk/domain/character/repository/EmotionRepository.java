package com.victory.evertalk.domain.character.repository;

import com.victory.evertalk.domain.character.entity.Emotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmotionRepository extends JpaRepository<Emotion, Integer> {

    Optional<Emotion> findByEmotion(String emotion);

}
