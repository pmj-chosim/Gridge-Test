package com.example.gridge.controller.user;


import com.example.gridge.controller.user.dto.UserSimpleResponseDto;
import dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@Valid @PathVariable Integer id) {
        UserResponseDto user = adminService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    // - `status`: 유저 상태별 필터링 (`active`, `nonactive`, `banned`)
    //- `username` : 유저 이름
    //- `date`, `end_date`: 회원가입 날짜 (YYYY-MM-DD)
    @GetMapping("/users")
    public ResponseEntity<Page<UserSimpleResponseDto>> getAllUsers(
            @Valid @RequestParam Integer page, @Valid @RequestParam Integer size,
            @Valid @RequestParam String username, @Valid @RequestParam String status,
            @Valid @RequestParam LocalDate date){

        Page<UserSimpleResponseDto> users=adminService.getAllUsers(page, size, username, status, date);
        return ResponseEntity.ok(users);
    }

}
