package com.victory.evertalk.domain.episode.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AIUserAnswerFeedbackResponseDto {

    Integer affinity;
    String next_utterance;
    String emotion;
    @JsonProperty("isGrammarError")
    boolean isGrammarError;
    String last_user_input;
    String last_user_correction_input;

}
