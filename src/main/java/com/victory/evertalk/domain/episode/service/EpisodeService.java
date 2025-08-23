package com.victory.evertalk.domain.episode.service;

import com.victory.evertalk.domain.character.entity.Character;
import com.victory.evertalk.domain.character.entity.Face;
import com.victory.evertalk.domain.character.entity.Likeability;
import com.victory.evertalk.domain.character.service.CharacterReadService;
import com.victory.evertalk.domain.character.service.FaceReadService;
import com.victory.evertalk.domain.character.service.LikeabilityReadService;
import com.victory.evertalk.domain.episode.dto.EpisodeListDetailResponseDto;
import com.victory.evertalk.domain.episode.dto.EpisodeListResponseDto;
import com.victory.evertalk.domain.episode.dto.StartEpisodeResponseDto;
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
    private final FaceReadService faceReadService;
    private final CharacterReadService characterReadService;

    private Integer characterId = 1;

    public EpisodeListResponseDto searchEpisodeList(Integer userId){

        userReadService.findUserByIdOrElseThrow(userId);

        Integer likeability = likeabilityReadService.findLikeabilityByUserIdAndCharacterId(userId, characterId).getCount();
        Integer progress = progressReadService.findProgressByUserIdAndCharacterId(userId).getEpisodeNum();

        List<Episode> episodeList = episodeReadService.findEpisodeListByUserIdAndCharacterId(userId);

        List<EpisodeListDetailResponseDto> episodeDetailList = new ArrayList<>();
        for(Episode episode:episodeList){
            EpisodeListDetailResponseDto episodeDetail = EpisodeListDetailResponseDto.builder()
                    .episodeId(episode.getEpisodeId())
                    .episodeTitle(episode.getTitle())
                    .build();
            episodeDetailList.add(episodeDetail);
        }

        return EpisodeListResponseDto.builder()
                .likeability(likeability)
                .progress(progress)
                .episodeDetail(episodeDetailList)
                .build();
    }

    public StartEpisodeResponseDto StartEpisode(Integer userId, Integer episodeId) {
        User user = userReadService.findUserByIdOrElseThrow(userId);

        Episode episode = episodeReadService.findEpisodeByEpisodeId(episodeId);

        Face face = faceReadService.findFaceByCharacterId(characterId);
        Character character = characterReadService.findCharacterByCharacterId(characterId);

        Likeability likeability = likeabilityReadService.findLikeabilityByUserIdAndCharacterId(userId, characterId);

        return StartEpisodeResponseDto.builder()
                .chat(episode.getStart())
                .img(face.getUrl())
                .changeLike(0)
                .characterName(character.getCharacterName())
                .feedback("")
                .lastChat(false)
                .nickname(user.getNickname())
                .likeability(likeability.getCount())
                .build();
    }

}
