package com.victory.evertalk.domain.expression.controller;

import com.victory.evertalk.domain.expression.dto.ExpressionResponseDto;
import com.victory.evertalk.domain.expression.service.ExpressionService;
import com.victory.evertalk.global.auth.token.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/expression")
public class ExpressionController {

    private final ExpressionService expressionService;

    @GetMapping("/{episodeId}")
    public List<ExpressionResponseDto> searchEpisodeList(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Integer episodeId) {
        return expressionService.searchWrongExpression(userPrincipal.getUserId(), episodeId);
    }


}
