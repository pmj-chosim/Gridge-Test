package com.example.gridge.service;

import com.example.gridge.controller.payment.dto.SubscriptionResponseDto;
import com.example.gridge.repository.SubscriptionRepository;
import com.example.gridge.repository.entity.payment.Subscription;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    @Transactional(readOnly = true)
    public SubscriptionResponseDto getSubscriptionByUserId(Integer userId) {
        // 1. userId를 사용하여 DB에서 구독 정보를 찾습니다.
        Subscription subscription = subscriptionRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("No subscription found for this user."));

        // 2. 찾은 엔티티를 DTO로 변환하여 반환합니다.
        return SubscriptionResponseDto.from(subscription);
    }
}