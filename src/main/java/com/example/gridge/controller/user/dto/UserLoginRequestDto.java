package com.example.gridge.controller.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "유저 로그인 요청 Dto")
@Getter
@AllArgsConstructor
public class UserLoginRequestDto {
    @Schema(description = "유저 이름", example = "user1", required = true)
    @Size(min=2, max=20, message="Name must be between 2 and 20 characters")
    private String name;

    @Schema(description = "유저 비밀번호", example = "password123", required = true)
    @Size(min=6, max=20, message="Password must be between 6 and 20 characters")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$", message = "Password must contain at least one letter and one number")
    private String password;
}
