package com.example.gridge.service;

import com.example.gridge.controller.user.dto.UserCreateRequestDto;
import com.example.gridge.controller.user.dto.UserLoginRequestDto;
import com.example.gridge.controller.user.dto.UserResponseDto;
import com.example.gridge.controller.user.dto.UserSimpleResponseDto;
import com.example.gridge.repository.UserRepository;
import com.example.gridge.repository.entity.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    UserRepository userRepository;

    public UserResponseDto login(UserLoginRequestDto request){
        User user=userRepository.findByName(request.getName()).
                orElseThrow(()->new RuntimeException("User not found"));

        //로그인 검증 로직 추가 필요 (ex. password check)
        return UserResponseDto.from(user);
    }

    public UserResponseDto create(UserCreateRequestDto request){
        User user=User.create(
                request.getName(),
                request.getPassword(),
                request.getPhoneNumber(),
                ...

        );
        userRepository.save(user);
        return UserResponseDto.from(user);
    }

    public UserResponseDto resetPassword(UserCreateRequestDto request){
        User user=userRepository.findByName(request.getName()).
                orElseThrow(()->new RuntimeException("User not found"));

        user.resetPassword(request.getPassword());
        userRepository.save(user);
        return UserResponseDto.from(user);
    }

    public UserResponseDto getUserById(Integer id){
        User user=userRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("User not found"));
        return UserResponseDto.from(user);
    }

    //다른 쿼리들에 대해서도 처리 필요
    public Page<UserSimpleResponseDto> getAllUsers(int page, int size){
        Pageable pageable= PageRequest.of(page,size, Sort.by("id").descending());
        Page<User> users=userRepository.findAll(pageable);
        return users.map(UserSimpleResponseDto::from);
    }

}
