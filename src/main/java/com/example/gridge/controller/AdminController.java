package com.example.gridge.controller;


import com.example.gridge.controller.user.dto.UserSimpleResponseDto;
import dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.*;

import java.time.LocalDate;

@Tag(name="Admin", description="어드민 권한자만 접근 가능한 정보 API")
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @Operation(summary="특정 유저 정보 조회", description="특정 유저의 상세 정보를 조회합니다. (어드민 권한 필요)")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@Valid @PathVariable Integer id) {
        UserResponseDto user = adminService.getUserById(id);
        return ResponseEntity.ok(user);
    }


    // - `status`: 유저 상태별 필터링 (`active`, `nonactive`, `banned`)
    //- `username` : 유저 이름
    //- `date`, `end_date`: 회원가입 날짜 (YYYY-MM-DD)
    @Operation(summary="모든 유저 정보 조회(페이징)",
            description="모든 유저의 간단한 정보를 페이징 처리하여 조회합니다. (어드민 권한 필요)," +
                    "필터링 옵션: username, status(active, nonactive, banned), date(YYYY-MM-DD)")
    @GetMapping("/users")
    public ResponseEntity<Page<UserSimpleResponseDto>> getAllUsers(
            @Valid @RequestParam Integer page, @Valid @RequestParam Integer size,
            @Valid @RequestParam String username, @Valid @RequestParam String status,
            @Valid @RequestParam LocalDate date){

        Page<UserSimpleResponseDto> users=adminService.getAllUsers(page, size, username, status, date);
        return ResponseEntity.ok(users);
    }

}
