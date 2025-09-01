package com.example.gridge.controller.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.processing.Pattern;

@Getter
@AllArgsConstructor
public class UserCreateRequestDto {

    @Size(min=2, max=20, message="Name must be between 2 and 20 characters")
    private String name;

    @Size(min=6, max=20, message="Password must be between 6 and 20 characters")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$",
            message = "Password must contain at least one letter and one number")
    private String password;

    @Pattern(regexp = "^\\+82-010-\\d{4}-\\d{4}$", message = "Phone number must be in the format +82-10-xxxx-xxxx")
    private String phoneNumber;
}
