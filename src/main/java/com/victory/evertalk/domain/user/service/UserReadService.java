package com.victory.evertalk.domain.user.service;

import com.victory.evertalk.domain.user.entity.User;
import com.victory.evertalk.domain.user.repository.UserRepository;
import com.victory.evertalk.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.victory.evertalk.global.exception.ErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserReadService {

    private final UserRepository userRepository;

    public User findUserByEmailOrElseThrow(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BaseException(USER_NOT_FOUND));
    }

    public User findUserByIdOrElseThrow(Integer userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(USER_NOT_FOUND));
    }

}
