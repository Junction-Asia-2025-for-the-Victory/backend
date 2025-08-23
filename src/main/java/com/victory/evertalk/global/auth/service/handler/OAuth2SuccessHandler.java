package com.victory.evertalk.global.auth.service.handler;

import com.victory.evertalk.domain.user.entity.User;
import com.victory.evertalk.domain.user.service.UserReadService;
import com.victory.evertalk.global.auth.dto.TokenDto;
import com.victory.evertalk.global.auth.entity.Auth;
import com.victory.evertalk.global.auth.repository.AuthRepository;
import com.victory.evertalk.global.auth.service.AuthService;
import com.victory.evertalk.global.auth.token.UserPrincipal;
import com.victory.evertalk.global.exception.BaseException;
import com.victory.evertalk.global.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.victory.evertalk.global.exception.ErrorCode.AUTH_NOT_FOUND;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final AuthService authService;
    private final AuthRepository authRepository;
    private final UserReadService userReadService;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        UserPrincipal oAuth2User = (UserPrincipal) authentication.getPrincipal();
        String email = oAuth2User.getEmail();
        User user = userReadService.findUserByEmailOrElseThrow(email);
        Auth auth = authRepository.findByUser(user)
                .orElseThrow(() -> new BaseException(AUTH_NOT_FOUND));

        // 토큰 생성
        TokenDto tokens = jwtUtil.generateTokens(email);

        // Refresh Token DB 저장
        authService.updateRefreshToken(tokens.getRefreshToken(), auth);

        // Refresh Token 쿠키에 저장
        authService.addRefreshTokenCookie(response, tokens.getRefreshToken());

        // Access Token 쿠키에 저장
        authService.addAccessTokenCookie(response, tokens.getAccessToken());

        // CORS 헤더 설정
        response.setHeader("Access-Control-Allow-Origin", frontendUrl);
        response.setHeader("Access-Control-Allow-Credentials", "true");

        if(user.getGender() == null){
            // 필수 정보 입력 안 되었을 경우 추가 정보 입력 url로 리다이렉트(url 수정 필요)ㄴ
            response.sendRedirect(frontendUrl + "/entry");
        } else {
            response.sendRedirect(frontendUrl + "/");
        }
    }


}
