package com.victory.evertalk.domain.episode.service;

import com.victory.evertalk.domain.episode.entity.Progress;
import com.victory.evertalk.domain.episode.repository.ProgressRepository;
import com.victory.evertalk.domain.user.entity.User;
import com.victory.evertalk.domain.character.entity.Character;
import com.victory.evertalk.global.exception.BaseException;
import com.victory.evertalk.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProgressReadService {

    private final ProgressRepository progressRepository;

    private Integer characterId = 1;

    public Progress findProgressByUserIdAndCharacterId(Integer userId) {
        return progressRepository.findByUser_UserIdAndCharacter_CharacterId(userId, characterId)
                .orElseGet(() -> Progress.builder()
                        .user(User.withId(userId))                       // 최소한의 참조
                        .character(Character.builder().characterId(characterId).build())
                        .episodeNum(0)
                        .build()
                );
    }


}
