package com.victory.evertalk.domain.episode.service;

import com.victory.evertalk.domain.episode.entity.Episode;
import com.victory.evertalk.domain.episode.repository.EpisodeRepository;
import com.victory.evertalk.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.victory.evertalk.global.exception.ErrorCode.EPISODE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class EpisodeReadService {

    private final EpisodeRepository episodeRepository;

    private Integer characterId = 1;

    public Episode findEpisodeByEpisodeId(Integer episodeId){
        return episodeRepository.findById(episodeId)
                .orElseThrow(() -> new BaseException(EPISODE_NOT_FOUND));
    }

    public List<Episode> findEpisodeListByUserIdAndCharacterId(Integer userId){
        return episodeRepository.findAllEpisodesByUserIdAndCharacterId(userId, characterId);
    };

}

