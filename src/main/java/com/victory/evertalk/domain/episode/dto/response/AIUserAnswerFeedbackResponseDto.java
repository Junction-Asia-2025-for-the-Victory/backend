package com.victory.evertalk.domain.episode.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AIUserAnswerFeedbackResponseDto {

    Integer affinity;
    String next_utterance;
    String emotion;
    boolean isGrammarError;
    String last_user_input;
    String last_user_correction_input;

}
