package com.victory.evertalk.domain.character.service;

import com.victory.evertalk.domain.character.repository.CharacterRepository;
import com.victory.evertalk.global.exception.BaseException;
import com.victory.evertalk.global.exception.ErrorCode;
import com.victory.evertalk.domain.character.entity.Character;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CharacterReadService {

    private final CharacterRepository characterRepository;

    public Character findCharacterByCharacterId(Integer characterId){
        return characterRepository.findById(characterId)
                .orElseThrow(() -> new BaseException(ErrorCode.CHARACTER_NOT_FOUND));
    }

}
