package com.victory.evertalk.domain.episode.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class StartEpisodeResponseDto {

    private String chat;
    private String img;
    private Integer likeability;
    private Integer changeLike;
    private String feedback;
    private boolean lastChat;
    private String nickname;
    private String characterName;

}
