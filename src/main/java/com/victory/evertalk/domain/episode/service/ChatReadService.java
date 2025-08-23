package com.victory.evertalk.domain.episode.service;

import com.victory.evertalk.domain.episode.entity.Chat;
import com.victory.evertalk.domain.episode.repository.ChatRepository;
import com.victory.evertalk.global.exception.BaseException;
import com.victory.evertalk.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatReadService {

    private final ChatRepository chatRepository;

    public Chat findChatByChatId(Integer chatId){
        return chatRepository.findById(chatId)
                .orElseThrow(() -> new BaseException(ErrorCode.CHAT_NOT_FOUND));
    }

}
