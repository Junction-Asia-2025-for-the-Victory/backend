package com.victory.evertalk.domain.episode.controller;

import com.victory.evertalk.domain.episode.dto.EpisodeListResponseDto;
import com.victory.evertalk.domain.episode.dto.StartEpisodeRequestDto;
import com.victory.evertalk.domain.episode.dto.StartEpisodeResponseDto;
import com.victory.evertalk.domain.episode.service.EpisodeService;
import com.victory.evertalk.global.auth.token.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/episode")
public class EpisodeController {

    private final EpisodeService episodeService;

    @GetMapping()
    public EpisodeListResponseDto searchEpisodeList(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return episodeService.searchEpisodeList(userPrincipal.getUserId());
    }

    @PostMapping("")
    public StartEpisodeResponseDto startEpisode(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody StartEpisodeRequestDto startEpisodeRequestDto){
        log.debug("여기 들어오긴 했니?");
        return episodeService.StartEpisode(userPrincipal.getUserId(), startEpisodeRequestDto.getEpisodeId());
    }

}
