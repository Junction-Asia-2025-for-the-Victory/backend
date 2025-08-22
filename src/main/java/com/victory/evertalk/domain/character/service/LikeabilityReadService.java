package com.victory.evertalk.domain.character.service;

import com.victory.evertalk.domain.character.entity.Likeability;
import com.victory.evertalk.domain.character.repository.LikeabilityRepository;
import com.victory.evertalk.domain.user.entity.User;
import com.victory.evertalk.global.exception.BaseException;
import com.victory.evertalk.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.victory.evertalk.domain.character.entity.Character;

import static com.victory.evertalk.global.exception.ErrorCode.LIKEABLILTY_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class LikeabilityReadService {

    private final LikeabilityRepository likeabilityRepository;

    public Likeability findLikeabilityByUserIdAndCharacterId(Integer userId, Integer characterId) {
        return likeabilityRepository.findByUser_UserIdAndCharacter_CharacterId(userId, characterId)
                .orElseGet(() -> Likeability.builder()
                        .user(User.withId(userId))
                        .character(Character.builder().characterId(characterId).build()) // 최소한의 Character 참조
                        .count(0) // likeability 기본값
                        .build()
                );
    }

}
