package com.victory.evertalk.domain.character.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.victory.evertalk.domain.character.entity.Character;

public interface CharacterRepository extends JpaRepository<Character, Integer> {
}
