package com.example.gridge.controller.user.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponseDto {
    private Integer id;
    private String name;
    private String status;
    private String phoneNumber;
    private String createdAt;
    private String lastLoginAt;
    private Boolean isAdmin;

    public static UserResponseDto from(User user){
        return new UserResponseDto(
          user.getId(),
          user.getName(),
          user.getStatus().toString(),
          user.getPhoneNumber(),
          user.getCreatedAt().toString(),
          user.getLastLoginAt().toString(),
          user.getIsAdmin()
        );
    }
}
