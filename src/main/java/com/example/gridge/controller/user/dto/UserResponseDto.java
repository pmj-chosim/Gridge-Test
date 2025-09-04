package com.example.gridge.controller.user.dto;


import com.example.gridge.repository.entity.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "유저 객체 응답 Dto")
@Getter
@AllArgsConstructor
public class UserResponseDto {

    @Schema(description = "유저 ID", example = "1")
    private Integer id;

    @Schema(description = "유저 이름", example = "imagroot")
    private String name;

    @Schema(description = "유저 상태", example = "ACTIVE")
    private String status;

    @Schema(description = "유저 전화번호", example = "+82-10-1234-5678")
    private String phoneNumber;

    @Schema(description = "유저 생성 일자", example = "2023-10-01T12:34:56")
    private String createdAt;

    @Schema(description = "유저 마지막 로그인 일자", example = "2023-10-10T08:00:00")
    private String lastLoginAt;

    @Schema(description = "유저 관리자 여부", example = "false")
    private Boolean isAdmin;

    public static UserResponseDto from(User user){
        return new UserResponseDto(
          user.getId(),
          user.getName(),
          user.getStatus(),
          user.getPhonenumber(),
          user.getCreatedAt().toString(),
          user.getLastLoginAt().toString(),
          user.getIsAdmin()
        );
    }
}
