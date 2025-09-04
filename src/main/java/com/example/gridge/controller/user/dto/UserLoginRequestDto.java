package com.example.gridge.controller.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "유저 로그인 요청 Dto")
@Getter
@AllArgsConstructor
public class UserLoginRequestDto {
    @Schema(description = "유저 이름", example = "user1", required = true)
    private String name;

    @Schema(description = "유저 비밀번호", example = "password123", required = true)
    private String password;
}
