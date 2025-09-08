package com.example.gridge.service;

import com.example.gridge.controller.payment.dto.PaymentCancelRequestDto;
import com.example.gridge.controller.payment.dto.PaymentCompleteRequestDto;
import com.example.gridge.controller.payment.dto.PaymentProcessRequestDto;
import com.example.gridge.controller.payment.dto.SubscriptionResponseDto;
import com.example.gridge.repository.PaymentRepository;
import com.example.gridge.repository.UserRepository;
import com.example.gridge.repository.entity.payment.Payment;
import com.example.gridge.repository.entity.payment.PaymentStatus;
import com.example.gridge.repository.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    @Transactional
    public void processPayment(User user, PaymentProcessRequestDto request) {
        // 결제 준비 로직
        // 1. DTO에 담긴 결제 정보를 바탕으로 Payment 엔티티를 생성합니다.
        Payment payment = Payment.create(
                user,
                request.getAmount(),
                PaymentStatus.PENDING, // 초기 상태는 PENDING으로 설정
                request.getMerchantUid()
        );
        // 2. Payment 엔티티를 DB에 저장합니다.
        paymentRepository.save(payment);
    }

    @Transactional
    public void completePayment(User user, PaymentCompleteRequestDto request) {
        // 결제 완료 로직
        // 1. merchantUid로 DB에서 기존 Payment 엔티티를 찾습니다.
        Payment payment = paymentRepository.findByTransactionId(request.getMerchantUid())
                .orElseThrow(() -> new RuntimeException("Invalid merchantUid."));

        // 2. 포트원에서 받은 impUid를 엔티티에 저장합니다.
        payment.updateImpUid(request.getImpUid());

        // 3. 결제 상태를 PAID로 변경합니다.
        payment.updateStatus(PaymentStatus.PAID);
        paymentRepository.save(payment);
    }

    @Transactional
    public void cancelPayment(User user, PaymentCancelRequestDto request) {
        // 결제 취소 로직
        // 1. merchantUid로 DB에서 Payment 엔티티를 찾습니다.
        Payment payment = paymentRepository.findByTransactionId(request.getMerchantUid())
                .orElseThrow(() -> new RuntimeException("Invalid merchantUid."));

        // 2. 결제 취소 권한을 확인합니다.
        if (!payment.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not authorized to cancel this payment.");
        }

        // 3. 결제 상태를 CANCELLED로 변경합니다.
        payment.updateStatus(PaymentStatus.CANCELLED);
        paymentRepository.save(payment);
    }
}