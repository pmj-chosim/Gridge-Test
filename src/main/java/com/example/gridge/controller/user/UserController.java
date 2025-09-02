package com.example.gridge.controller.user;

import com.example.gridge.controller.user.dto.UserCreateRequestDto;
import com.example.gridge.controller.user.dto.UserLoginRequestDto;
import com.example.gridge.service.user.UserService;
import dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name="User API", description="일반 유저가 접근 가능한 정보 API")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary="유저 로그인", description="유저가 자신의 계정으로 로그인합니다.")
    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@RequestBody @Valid UserLoginRequestDto request){

        UserResponseDto user = userService.login(request);
        return ResponseEntity.ok(user);
    }

    @Operation(summary="유저 회원가입", description="새로운 유저가 회원가입을 합니다.")
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody @Valid UserCreateRequestDto request){
        UserResponseDto user=userService.create(request);
        return ResponseEntity.ok(user);
    }

    @Operation(summary="유저 비밀번호 재설정", description="유저가 자신의 비밀번호를 재설정합니다.")
    @PostMapping("/reset-password")
    public UserResponseDto resetPassword(@RequestBody @Valid UserCreateRequestDto request){
        UserResponseDto user= userService.resetPassword(request);
        return ResponseEntity.ok(user);
    }

}
