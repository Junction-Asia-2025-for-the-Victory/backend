package com.victory.evertalk.domain.expression.service;

import com.victory.evertalk.domain.character.entity.Character;
import com.victory.evertalk.domain.character.repository.CharacterRepository;
import com.victory.evertalk.domain.episode.entity.Chat;
import com.victory.evertalk.domain.episode.entity.Episode;
import com.victory.evertalk.domain.episode.repository.ChatRepository;
import com.victory.evertalk.domain.episode.repository.EpisodeRepository;
import com.victory.evertalk.domain.expression.dto.ExpressionResponseDto;
import com.victory.evertalk.domain.expression.entity.Expression;
import com.victory.evertalk.domain.expression.repository.ExpressionRepository;
import com.victory.evertalk.domain.user.entity.User;
import com.victory.evertalk.domain.user.repository.UserRepository;
import com.victory.evertalk.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.victory.evertalk.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class ExpressionService {

    private final ExpressionRepository expressionRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final CharacterRepository characterRepository;
    private final EpisodeRepository episodeRepository;

    public List<ExpressionResponseDto> searchWrongExpression(Integer userId, Integer episodeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(USER_NOT_FOUND));

        Character character = characterRepository.findById(1)
                .orElseThrow(() -> new BaseException(CHARACTER_NOT_FOUND));

        Episode episode = episodeRepository.findById(episodeId)
                .orElseThrow(() -> new BaseException(EPISODE_NOT_FOUND));

        Chat chat = chatRepository.findByUserAndCharacterAndEpisode(user, character, episode)
                .orElseThrow(() -> new BaseException(CHAT_NOT_FOUND));

        List<Expression> expressions = expressionRepository.findByUserAndChat(user, chat);

        return expressions.stream()
                .map(expression -> new ExpressionResponseDto(
                        expression.getExpression(),
                        expression.getFeedback()
                ))
                .toList();
    }
}
