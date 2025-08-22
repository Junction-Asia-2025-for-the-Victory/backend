package com.victory.evertalk.domain.user.controller;

import com.victory.evertalk.domain.user.dto.SetUserProfileRequestDto;
import com.victory.evertalk.domain.user.service.UserService;
import com.victory.evertalk.global.auth.service.AuthService;
import com.victory.evertalk.global.auth.token.UserPrincipal;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    @GetMapping("/withdraw")
    public void withdrawUser(@AuthenticationPrincipal UserPrincipal userPrincipal, HttpServletResponse response){
        userService.withdrawUser(userPrincipal.getUserId());
        authService.logout(userPrincipal.getUser(), response);
    }

    @PatchMapping("/profile")
    public void setUserProfile(@AuthenticationPrincipal UserPrincipal userPrincipal, SetUserProfileRequestDto userProfileRequestDto){
        userService.setUserProfile(userPrincipal.getUserId(), userProfileRequestDto);
    }

}
