package com.example.gridge.controller.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSimpleResponseDto {
    private Integer id;
    private String name;
    private String status;
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