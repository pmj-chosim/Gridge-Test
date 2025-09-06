package com.example.gridge.controller.user.dto;

import com.example.gridge.repository.entity.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "유저 객체 간단 응답 DTO")
public class UserSimpleResponseDto {

    @Schema(description = "유저 ID", example = "1")
    private Integer id;

    @Schema(description = "유저 이름", example = "john_doe")
    private String name;

    @Schema(description = "유저 상태", example = "ACTIVE")
    private String status;

    @Schema(description = "유저 생성 일자", example = "2023-10-01T12:34:56")
    private String createdAt;

    public static UserSimpleResponseDto from(User user) {
        return new UserSimpleResponseDto(
                user.getId(),
                user.getName(),
                user.getStatus().name(),
                user.getCreatedAt().toString()
        );
    }
}