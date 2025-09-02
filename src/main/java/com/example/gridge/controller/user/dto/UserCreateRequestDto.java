package com.example.gridge.controller.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.processing.Pattern;

@Schema
@Getter
@AllArgsConstructor
public class UserCreateRequestDto {

    @Schema(description="User's name(2~20)", example="john_doe", required=true)
    @Size(min=2, max=20, message="Name must be between 2 and 20 characters")
    private String name;

    @Schema(description="User's password(6~20)", example="Password123", required=true)
    @Size(min=6, max=20, message="Password must be between 6 and 20 characters")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$",
            message = "Password must contain at least one letter and one number")
    private String password;

    @Schema(description="User's phone number in the format +82-10-xxxx-xxxx", example="+82-10-1234-5678", required=true)
    @Pattern(regexp = "^\\+82-010-\\d{4}-\\d{4}$", message = "Phone number must be in the format +82-10-xxxx-xxxx")
    private String phoneNumber;
}
