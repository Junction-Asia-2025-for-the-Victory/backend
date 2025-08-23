package com.victory.evertalk.domain.episode.controller;

import com.victory.evertalk.domain.episode.dto.request.UserAnswerFeedbackRequestDto;
import com.victory.evertalk.domain.episode.dto.response.EpisodeListResponseDto;
import com.victory.evertalk.domain.episode.dto.request.StartEpisodeRequestDto;
import com.victory.evertalk.domain.episode.dto.response.StartEpisodeResponseDto;
import com.victory.evertalk.domain.episode.service.EpisodeService;
import com.victory.evertalk.domain.episode.service.SttService;
import com.victory.evertalk.global.auth.token.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/episode")
public class EpisodeController {

    private final EpisodeService episodeService;
    private final SttService sttService;

    @GetMapping()
    public EpisodeListResponseDto searchEpisodeList(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return episodeService.searchEpisodeList(userPrincipal.getUserId());
    }

    @PostMapping("")
    public StartEpisodeResponseDto startEpisode(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody StartEpisodeRequestDto startEpisodeRequestDto){
        return episodeService.StartEpisode(userPrincipal.getUserId(), startEpisodeRequestDto.getEpisodeId());
    }

    @PostMapping("/chat")
    public StartEpisodeResponseDto userAnswer(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam("chatId") Integer chatId, @RequestPart("audioFile") MultipartFile audioFile) {

        log.debug("chatId: ", chatId);

        String text = sttService.transcribeWebm(audioFile);
        return episodeService.userAnswer(userPrincipal.getUserId(), chatId, text);

    }

}
