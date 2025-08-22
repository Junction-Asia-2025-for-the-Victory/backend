package com.victory.evertalk.domain.episode.repository;

import com.victory.evertalk.domain.episode.entity.Progress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProgressRepository extends JpaRepository<Progress, Integer> {

    Optional<Progress> findByUser_UserIdAndCharacter_CharacterId(Integer userId, Integer progressId);

}
