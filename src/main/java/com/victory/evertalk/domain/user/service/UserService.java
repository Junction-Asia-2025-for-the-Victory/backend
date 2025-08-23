package com.victory.evertalk.domain.user.service;

import com.victory.evertalk.domain.user.dto.SetUserProfileRequestDto;
import com.victory.evertalk.domain.user.entity.User;
import com.victory.evertalk.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserReadService userReadService;
    private final UserRepository userRepository;

    @Transactional
    public void withdrawUser (Integer userId) {
        User user = userReadService.findUserByIdOrElseThrow(userId);
        user.withdrawUser();
        userRepository.save(user);
    }

    @Transactional
    public void setUserProfile(Integer userId, @RequestBody SetUserProfileRequestDto userProfile){
        User user = userReadService.findUserByIdOrElseThrow(userId);
        user.addGender(userProfile.getGender());
        user.changeNickname(userProfile.getNickname());
        userRepository.save(user);
    }

}
