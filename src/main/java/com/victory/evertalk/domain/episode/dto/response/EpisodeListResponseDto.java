package com.victory.evertalk.domain.episode.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class EpisodeListResponseDto {

    private Integer likeability;
    private Integer progress;
    private List<EpisodeListDetailResponseDto> episodeDetail;

}
