package com.victory.evertalk.domain.episode.service;

import com.victory.evertalk.domain.character.service.LikeabilityReadService;
import com.victory.evertalk.domain.episode.dto.EpisodeListDetailResponseDto;
import com.victory.evertalk.domain.episode.dto.EpisodeListResponseDto;
import com.victory.evertalk.domain.episode.entity.Episode;
import com.victory.evertalk.domain.episode.repository.EpisodeRepository;
import com.victory.evertalk.domain.user.entity.User;
import com.victory.evertalk.domain.user.service.UserReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EpisodeService {

    private final EpisodeReadService episodeReadService;
    private final UserReadService userReadService;
    private final LikeabilityReadService likeabilityReadService;
    private final ProgressReadService progressReadService;

    private Integer characterId = 1;

    public EpisodeListResponseDto searchEpisodeList(Integer userId){

        userReadService.findUserByIdOrElseThrow(userId);

        Integer likeability = likeabilityReadService.findLikeabilityByUserIdAndCharacterId(userId, characterId).getLikeabilityId();
        Integer progress = progressReadService.findProgressByUserIdAndCharacterId(userId).getProgressId();

        List<Episode> episodeList = episodeReadService.findEpisodeListByUserIdAndCharacterId(userId);

        List<EpisodeListDetailResponseDto> episodeDetailList = new ArrayList<>();
        for(Episode episode:episodeList){
            EpisodeListDetailResponseDto episodeDetail = EpisodeListDetailResponseDto.builder()
                    .episodeId(episode.getEpisodeId())
                    .episodeTitle(episode.getTitle())
                    .build();
        }

        return EpisodeListResponseDto.builder()
                .likeability(likeability)
                .progress(progress)
                .episodeDetail(episodeDetailList)
                .build();
    }

}
