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

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@RequestBody @Valid UserLoginRequestDto request){

        UserResponseDto user = userService.login(request);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody @Valid UserCreateRequestDto request){
        UserResponseDto user=userService.create(request);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/reset-password")
    public UserResponseDto resetPassword(@RequestBody @Valid UserCreateRequestDto request){
        UserResponseDto user= userService.resetPassword(request);
        return ResponseEntity.ok(user);
    }

}
