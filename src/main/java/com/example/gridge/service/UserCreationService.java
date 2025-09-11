package com.example.gridge.service;

import com.example.gridge.controller.user.dto.UserCreateRequestDto;
import com.example.gridge.controller.user.dto.UserLoginRequestDto;
import com.example.gridge.controller.user.dto.UserResponseDto;
import com.example.gridge.controller.user.dto.UserSimpleResponseDto;
import com.example.gridge.repository.UserRepository;
import com.example.gridge.repository.entity.user.ActiveLevel;
import com.example.gridge.repository.entity.user.LoginType;
import com.example.gridge.repository.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserCreationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public UserResponseDto create(UserCreateRequestDto request) {
        String encodedPassword = null;
        String socialId = null;

        if (request.getLoginType() == LoginType.GENERAL) {
            if (request.getPassword() == null || request.getPassword().length() < 6) {
                throw new IllegalArgumentException("자체 로그인 시 비밀번호는 6자 이상이어야 합니다.");
            }
            encodedPassword = passwordEncoder.encode(request.getPassword());
        } else {
            if (request.getSocialId() == null) {
                throw new IllegalArgumentException("소셜 로그인 시 socialId는 필수입니다.");
            }
            socialId = request.getSocialId();
        }

        User user = User.create(
                request.getName(),
                encodedPassword,
                request.getPhoneNumber(),
                false,
                request.getBirthDate(),
                request.getLoginType(),
                socialId // socialId 필드를 User.create() 메서드에 추가했습니다.
        );

        userRepository.save(user);
        return UserResponseDto.from(user);
    }


    @Transactional
    public UserResponseDto updateUserStatus(Integer userId, ActiveLevel newStatus) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setActiveLevel(newStatus);
        userRepository.save(user);
        return UserResponseDto.from(user);
    }

    @Transactional
    public UserResponseDto resetPassword(User user, UserLoginRequestDto request) {
        // 이미 인증된 사용자이므로, user 객체를 사용해 비밀번호를 변경합니다.
        String newPassword = passwordEncoder.encode(request.getPassword());
        user.resetPassword(newPassword);
        userRepository.save(user);
        return UserResponseDto.from(user);
    }

    @Transactional(readOnly = true)
    public UserResponseDto getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return UserResponseDto.from(user);
    }

    @Transactional
    public UserResponseDto updateUserAdminStatus(Integer userId, Boolean isAdmin) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setIsAdmin(isAdmin);
        user.setRoles(isAdmin);

        userRepository.save(user);
        return UserResponseDto.from(user);
    }


    @Transactional(readOnly = true)
    public Page<UserSimpleResponseDto> getAllUsers(
            Integer page,
            Integer size,
            Optional<String> username,
            Optional<ActiveLevel> status,
            Optional<LocalDate> startDate,
            Optional<LocalDate> endDate) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Page<User> users = userRepository.findByNameContainingAndStatusAndBirthDateBetween(
                username.orElse(null),
                status.orElse(null),
                startDate.orElse(null),
                endDate.orElse(null),
                pageable
        );

        return users.map(UserSimpleResponseDto::from);
    }
}