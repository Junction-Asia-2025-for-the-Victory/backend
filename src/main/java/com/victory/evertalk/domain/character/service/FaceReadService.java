package com.victory.evertalk.domain.character.service;

import com.victory.evertalk.domain.character.entity.Face;
import com.victory.evertalk.domain.character.repository.FaceRepository;
import com.victory.evertalk.global.exception.BaseException;
import com.victory.evertalk.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FaceReadService {

    private final FaceRepository faceRepository;

    public Face findFaceByCharacter_CharacterIdAndEmotion_EmotionId(Integer characterId, Integer emotionId){
        return faceRepository.findFaceByCharacter_CharacterIdAndEmotion_EmotionId(characterId, emotionId)
                .orElseThrow(() -> new BaseException(ErrorCode.FACE_NOT_FOUND));
    }

}
