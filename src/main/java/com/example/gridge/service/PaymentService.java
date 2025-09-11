package com.example.gridge.service;

import com.example.gridge.controller.payment.dto.*;
import com.example.gridge.controller.user.dto.UserResponseDto;
import com.example.gridge.repository.PaymentRepository;
import com.example.gridge.repository.SubscriptionRepository;
import com.example.gridge.repository.UserRepository;
import com.example.gridge.repository.entity.payment.Payment;
import com.example.gridge.repository.entity.payment.PaymentStatus;
import com.example.gridge.repository.entity.payment.Subscription;
import com.example.gridge.repository.entity.payment.SubscriptionStatus;
import com.example.gridge.repository.entity.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
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
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    @Transactional
    public PaymentResponseDto processPayment(User user, PaymentProcessRequestDto request) {
        // 1. 이미 진행 중인 동일한 merchantUid 결제가 있는지 확인
        paymentRepository.findByTransactionIdAndStatus(request.getMerchantUid(), PaymentStatus.PENDING)
                .ifPresent(p -> {
                    throw new RuntimeException("Payment with the same transaction ID is already in PENDING status.");
                });

        // 2. 구독 정보가 없으면 새로 생성, 있으면 기존 구독 가져오기
        Subscription subscription = subscriptionRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Subscription newSub = Subscription.create(user, SubscriptionStatus.INACTIVE, 12);
                    return subscriptionRepository.save(newSub);
                });

        // 3. 결제 준비 로직
        Payment payment = Payment.create(
                user,
                request.getAmount(),
                PaymentStatus.PENDING,
                request.getMerchantUid()
        );

        // 4. Payment와 Subscription 연결
        payment.setSubscription(subscription);
        subscription.addPayment(payment);

        Payment savedPayment = paymentRepository.save(payment);
        return PaymentResponseDto.from(savedPayment);
    }

    @Transactional
    public PaymentResponseDto completePayment(User user, PaymentCompleteRequestDto request) {
        Payment payment = paymentRepository.findByTransactionId(request.getMerchantUid())
                .orElseThrow(() -> new RuntimeException("Invalid merchantUid."));

        payment.updateImpUid(request.getImpUid());
        payment.updateStatus(PaymentStatus.PAID);
        Payment savedPayment = paymentRepository.save(payment);

        // 구독 상태를 ACTIVE로 변경
        Subscription subscription = subscriptionRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Subscription not found for this user."));
        subscription.setType(SubscriptionStatus.ACTIVE);
        subscriptionRepository.save(subscription);

        return PaymentResponseDto.from(savedPayment);
    }

    @Transactional
    public void cancelPayment(User user, PaymentCancelRequestDto request) {
        Payment payment = paymentRepository.findByTransactionId(request.getImpUid())
                .orElseThrow(() -> new RuntimeException("Invalid merchantUid."));

        if (!payment.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not authorized to cancel this payment.");
        }

        payment.updateStatus(PaymentStatus.CANCELLED);
        Payment savedPayment = paymentRepository.save(payment);

        // 구독 상태를 INACTIVE로 변경
        Subscription subscription = subscriptionRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Subscription not found for this user."));
        subscription.setType(SubscriptionStatus.INACTIVE);
        subscriptionRepository.save(subscription);

        // subscription 상태를 INACTIVE로 변경
        subscription.setType(SubscriptionStatus.CANCELLED);
    }
    // 관리자용 메서드
    @Transactional(readOnly = true)
    public Page<PaymentResponseDto> getAllPayments(
            Integer page, Integer size,
            Optional<LocalDate> startFindDate,
            Optional<LocalDate> endFindDate,
            Optional<PaymentStatus> status,
            Optional<Integer> userId
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        // TODO: Repository에 필터링 쿼리 구현 필요
        Page<Payment> payments = paymentRepository.findAll(pageable);
        return payments.map(PaymentResponseDto::from);
    }
}
