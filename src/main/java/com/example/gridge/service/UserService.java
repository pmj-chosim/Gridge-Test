package com.example.gridge.service;

import com.example.gridge.repository.UserRepository;
import com.example.gridge.repository.entity.user.ActiveLevel;
import com.example.gridge.repository.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 유저입니다 - username : " + username));

        if (user.getActiveLevel() == ActiveLevel.INACTIVE) {
            throw new RuntimeException("휴면 계정입니다. 계정을 활성화해주세요.");
        }
        if (user.getActiveLevel() == ActiveLevel.BANNED) {
            throw new RuntimeException("차단된 계정입니다.");
        }

        // 개인정보처리 동의 만료 확인 (1년 기준)
        if (ChronoUnit.DAYS.between(user.getLastConsentDate(), LocalDateTime.now()) > 365) {
            throw new RuntimeException("개인정보처리 동의가 만료되었습니다. 다시 동의해주세요.");
        }

        return user;
    }

    @Transactional
    public void updateConsent(User user) {
        user.updateConsentDate();
        userRepository.save(user);
    }


}