package com.victory.evertalk.domain.episode.repository;

import com.victory.evertalk.domain.character.entity.Character;
import com.victory.evertalk.domain.episode.entity.Chat;
import com.victory.evertalk.domain.episode.entity.Episode;
import com.victory.evertalk.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Integer> {
    Optional<Chat> findByUserAndCharacterAndEpisode(User user, Character character, Episode episode);
}
