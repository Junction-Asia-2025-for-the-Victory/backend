package com.victory.evertalk.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SetUserProfileRequestDto {

    String nickname;
    String gender;
}
