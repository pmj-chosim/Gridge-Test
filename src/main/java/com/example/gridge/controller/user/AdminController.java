package com.example.gridge.controller.user;


import com.example.gridge.controller.user.dto.UserResponseDto;
import com.example.gridge.controller.user.dto.UserSimpleResponseDto;
import com.example.gridge.repository.entity.user.ActiveLevel;
import com.example.gridge.service.UserCreationService;
import com.example.gridge.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.*;

import java.time.LocalDate;
import java.util.Optional;

@Tag(name="Admin", description="어드민 권한자만 접근 가능한 정보 API")
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserCreationService userService;


    @Secured("ROLE_ADMIN")
    @Operation(summary="특정 유저 정보 조회", description="특정 유저의 상세 정보를 조회합니다. (어드민 권한 필요)")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Integer id) {
        UserResponseDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @Secured("ROLE_ADMIN")
    @Operation(summary="모든 유저 정보 조회(페이징)",
            description="모든 유저의 간단한 정보를 페이징 처리하여 조회합니다. (어드민 권한 필요)," +
                    "필터링 옵션: username, status(active, nonactive, banned), date(YYYY-MM-DD)")
    @GetMapping("/users")
    public ResponseEntity<Page<UserSimpleResponseDto>> getAllUsers(
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestParam(required = false) Optional<String> username,
            @RequestParam(required = false) Optional<ActiveLevel> status,
            @RequestParam(required = false) Optional<LocalDate> startDate,
            @RequestParam(required = false) Optional<LocalDate> endDate){

        Page<UserSimpleResponseDto> users = userService.getAllUsers(
                page, size, username, status, startDate, endDate);
        return ResponseEntity.ok(users);
    }

}
