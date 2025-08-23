package com.victory.evertalk.global.common.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.victory.evertalk.domain.episode.dto.request.UserAnswerFeedbackRequestDto;
import com.victory.evertalk.domain.episode.dto.response.AIUserAnswerFeedbackResponseDto;
import com.victory.evertalk.global.exception.BaseException;
import com.victory.evertalk.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class FastApiClientService {

    private final WebClient fastApiWebClient;
    private final ObjectMapper objectMapper;

    public AIUserAnswerFeedbackResponseDto sendUserAnswerToFastApi(UserAnswerFeedbackRequestDto fastApiRequestDto){
        AIUserAnswerFeedbackResponseDto responseDto = fastApiWebClient.post()
                .uri("/ai/api/v1/chat")
                .bodyValue(fastApiRequestDto)
                .retrieve()
                .bodyToMono(AIUserAnswerFeedbackResponseDto.class)
                .block();

        if(responseDto == null){
            log.debug("삐쌍!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! fast API에서 null 반환됨!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            throw new BaseException(ErrorCode.FAST_API_RESPONSE_NULL);
        }

        return responseDto;
    }

}
