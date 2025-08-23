package com.victory.evertalk.domain.episode.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserAnswerFeedbackRequestDto {

    String character_name;
    String character_info;
    String background_situation;
    List<ChatDetailDto> previous_chat;
    Integer affinity;
    String last_user_input;
    String user_nickname;
    String user_gender;

}
