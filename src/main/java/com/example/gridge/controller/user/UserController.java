package com.example.gridge.controller.user;

import com.example.gridge.controller.user.dto.UserCreateRequestDto;
import com.example.gridge.controller.user.dto.UserLoginRequestDto;
import com.example.gridge.controller.user.dto.UserResponseDto;
import com.example.gridge.repository.entity.user.User;
import com.example.gridge.service.UserCreationService;
import com.example.gridge.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.*;


@Tag(name="User API", description="일반 유저가 접근 가능한 정보 API")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserCreationService userCreationService;
    private final AuthenticationManager authenticationManager;


    /*
    @Operation(summary="유저 로그인", description="유저가 자신의 계정으로 로그인합니다.")
    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@RequestBody @Valid UserLoginRequestDto request){
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getName(), request.getPassword());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(UserResponseDto.from(user));
    }*/

    @Operation(summary="유저 회원가입", description="새로운 유저가 회원가입을 합니다.")
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody @Valid UserCreateRequestDto request){
        UserResponseDto user=userCreationService.create(request);
        return ResponseEntity.status(201).body(user);
    }

    @Operation(summary="유저 비밀번호 재설정", description="유저가 자신의 비밀번호를 재설정합니다.")
    @PostMapping("/reset-password")
    public ResponseEntity<UserResponseDto> resetPassword(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid UserLoginRequestDto request){
        UserResponseDto updatedUser = userCreationService.resetPassword(user, request);
        return ResponseEntity.ok(updatedUser);
    }


}
