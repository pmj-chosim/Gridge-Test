package com.example.gridge.service;

import com.example.gridge.controller.user.dto.UserCreateRequestDto;
import com.example.gridge.controller.user.dto.UserLoginRequestDto;
import com.example.gridge.controller.user.dto.UserResponseDto;
import com.example.gridge.controller.user.dto.UserSimpleResponseDto;
import com.example.gridge.repository.UserRepository;
import com.example.gridge.repository.entity.user.ActiveLevel;
import com.example.gridge.repository.entity.user.User;
import com.example.gridge.repository.entity.user.VisibleStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public UserResponseDto login(UserLoginRequestDto request) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getName(), request.getPassword());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();
        return UserResponseDto.from(user);
    }

    @Transactional
    public UserResponseDto create(UserCreateRequestDto request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.create(
                request.getName(),
                encodedPassword,
                request.getPhoneNumber(),
                false,
                request.getBirthDate(),
                request.getLoginType()
        );

        userRepository.save(user);

        return UserResponseDto.from(user);
    }

    @Transactional
    public UserResponseDto resetPassword(UserLoginRequestDto request) {
        User user = userRepository.findByName(request.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String newEncodedPassword = passwordEncoder.encode(request.getPassword());
        user.resetPassword(newEncodedPassword);

        userRepository.save(user);

        return UserResponseDto.from(user);
    }

    @Transactional(readOnly = true)
    public UserResponseDto getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 유저입니다 - username : " + username));
    }
}