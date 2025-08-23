package com.victory.evertalk.domain.character.service;

import com.victory.evertalk.domain.character.entity.Emotion;
import com.victory.evertalk.domain.character.repository.EmotionRepository;
import com.victory.evertalk.global.exception.BaseException;
import com.victory.evertalk.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmotionReadService {

    private final EmotionRepository emotionRepository;

    public Emotion findEmotionByEmotion(String emotion){
        return emotionRepository.findByEmotion(emotion)
                .orElseThrow(() -> new BaseException(ErrorCode.EMOTION_NOT_FOUND));
    }

}
