package com.victory.evertalk.domain.user.service;

import com.victory.evertalk.domain.user.entity.User;
import com.victory.evertalk.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}
