package com.victory.evertalk.domain.character.repository;

import com.victory.evertalk.domain.character.entity.Likeability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeabilityRepository extends JpaRepository<Likeability, Integer> {

    public Optional<Likeability> findByUser_UserIdAndCharacter_CharacterId(Integer userId, Integer characterId);

}
