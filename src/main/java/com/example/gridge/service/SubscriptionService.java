package com.example.gridge.service;

import com.example.gridge.controller.payment.dto.PaymentProcessRequestDto;
import com.example.gridge.controller.payment.dto.SubscriptionResponseDto;
import com.example.gridge.controller.user.dto.UserResponseDto;
import com.example.gridge.repository.PaymentRepository;
import com.example.gridge.repository.SubscriptionRepository;
import com.example.gridge.repository.UserRepository;
import com.example.gridge.repository.entity.payment.Payment;
import com.example.gridge.repository.entity.payment.PaymentStatus;
import com.example.gridge.repository.entity.payment.Subscription;
import com.example.gridge.repository.entity.payment.SubscriptionStatus;
import com.example.gridge.repository.entity.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final static Integer DURATION_MONTH = 12;


    @Transactional(readOnly = true)
    public SubscriptionResponseDto getSubscriptionByUserId(Integer userId) {
        // 1. userId를 사용하여 DB에서 구독 정보를 찾습니다.
        Subscription subscription = subscriptionRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("No subscription found for this user."));

        // 2. 찾은 엔티티를 DTO로 변환하여 반환합니다.
        return SubscriptionResponseDto.from(subscription);
    }
    // 관리자용 메서드
    @Transactional(readOnly = true)
    public Page<SubscriptionResponseDto> getAllSubscriptions(
            Integer page, Integer size,
            Optional<LocalDate> startFindDate,
            Optional<LocalDate> endFindDate,
            Optional<SubscriptionStatus> status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        // TODO: Repository에 필터링 쿼리 구현 필요 (예: findByStatusAndCreatedAtBetween...)
        Page<Subscription> subscriptions = subscriptionRepository.findAll(pageable);
        return subscriptions.map(SubscriptionResponseDto::from);
    }

    @Transactional
    public SubscriptionResponseDto createSubscriptionAdmin(Integer userId, PaymentProcessRequestDto dto) {
        User user=userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found."));
        Subscription subscription =Subscription.create(
                user,
                SubscriptionStatus.ACTIVE, // 관리자 생성 시 ACTIVE 상태로 바로 변경
                DURATION_MONTH
        );
        Payment payment=Payment.create(
                user,
                dto.getAmount(),
                PaymentStatus.PAID, // 관리자 생성 시 PAID 상태로 바로 변경
                dto.getMerchantUid()
        );
        payment.setSubscription(subscription);
        subscription.addPayment(payment);
        Subscription savedSubscription=subscriptionRepository.save(subscription);
        paymentRepository.save(payment);
        return SubscriptionResponseDto.from(savedSubscription);
    }

    @Transactional
    public SubscriptionResponseDto updateSubscription(Integer subscriptionId, PaymentProcessRequestDto dto) {
        Subscription subscription =subscriptionRepository.
                findById(subscriptionId).orElseThrow(() -> new RuntimeException("Subscription not found."));
        List<Payment> payments=subscription.getPayments();

        for(Payment p:payments){
            if(p.getTransactionId().equals(dto.getMerchantUid())){
                p.updateStatus(PaymentStatus.PAID);
                p.setPaymentAmount(dto.getAmount());
                paymentRepository.save(p);
            }
        }
        subscriptionRepository.save(subscription);
        return SubscriptionResponseDto.from(subscription);
    }

    @Transactional
    public SubscriptionResponseDto updateSubscriptionStatusAdmin(Integer subscriptionId, SubscriptionStatus status) {
        Subscription subscription =subscriptionRepository.
                findById(subscriptionId).orElseThrow(() -> new RuntimeException("Subscription not found."));
        subscription.setType(status);
        Subscription savedSubscription=subscriptionRepository.save(subscription);
        return SubscriptionResponseDto.from(savedSubscription);
    }

    @Transactional
    public void deleteSubscriptionAdmin(Integer subscriptionId) {
        Subscription subscription =subscriptionRepository.
                findById(subscriptionId).orElseThrow(() -> new RuntimeException("Subscription not found."));
        subscriptionRepository.delete(subscription);
    }
}
