package com.victory.evertalk.domain.episode.controller;

import com.victory.evertalk.domain.episode.dto.EpisodeListResponseDto;
import com.victory.evertalk.domain.episode.service.EpisodeService;
import com.victory.evertalk.global.auth.token.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/episode")
public class EpisodeController {

    private final EpisodeService episodeService;

    @GetMapping()
    public EpisodeListResponseDto searchEpisodeList(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return episodeService.searchEpisodeList(userPrincipal.getUserId());
    }

}
