package com.victory.evertalk.domain.episode.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class EpisodeListDetailResponseDto {

    private Integer episodeId;
    private String episodeTitle;

}
