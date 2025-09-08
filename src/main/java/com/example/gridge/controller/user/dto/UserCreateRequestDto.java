package com.example.gridge.controller.user.dto;

import com.example.gridge.repository.entity.user.LoginType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import jakarta.validation.constraints.*;
import jakarta.validation.constraints.Pattern;
import io.swagger.v3.oas.annotations.*;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema
@Getter
@AllArgsConstructor
public class UserCreateRequestDto {

    @Schema(description="User's name(2~20)", example="testuser", required=true)
    @Size(min=2, max=20, message="Name must be between 2 and 20 characters")
    private String name;

    @Schema(description="User's password(6~20)", example="Password123", required=true)
    @Size(min=6, max=20, message="Password must be between 6 and 20 characters")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$", message = "Password must contain at least one letter and one number")
    private String password;

    @Schema(description="User's phone number in the format +82-10-xxxx-xxxx", example="+82-10-1234-5678", required=true)
    @Pattern(regexp = "^\\+82-10-\\d{4}-\\d{4}$", message = "Phone number must be in the format +82-10-xxxx-xxxx")
    private String phoneNumber;

    @Schema(description="User's birth date in the format YYYY-MM-DD", example="1990-01-01", required=true)
    private LocalDate birthDate;

    @Schema(description="Login type (e.g., GENERAL, KAKAO, NAVER, GOOGLE)", example="GENERAL", required=true)
    @NotNull(message="Login type is required")
    private LoginType loginType;

    @Schema(description="Social ID (required for social logins)", example="1234567890")
    private String socialId;

}
