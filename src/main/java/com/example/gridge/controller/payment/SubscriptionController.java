package com.example.gridge.controller.payment;

import com.example.gridge.controller.payment.dto.PaymentProcessRequestDto;
import com.example.gridge.controller.payment.dto.PaymentResponseDto;
import com.example.gridge.controller.payment.dto.SubscriptionResponseDto;
import com.example.gridge.repository.entity.user.User;
import com.example.gridge.service.PaymentService;
import com.example.gridge.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
@Tag(name = "구독 API", description = "구독 관련 API")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;
    private final PaymentService paymentService;

    @Operation(summary = "내 구독 정보 조회 API", description = "현재 로그인한 사용자의 구독 정보를 조회하는 API")
    @GetMapping("/me")
    public ResponseEntity<SubscriptionResponseDto> getMySubscription(@AuthenticationPrincipal User user) {
        SubscriptionResponseDto subscription = subscriptionService.getSubscriptionByUserId(user.getId());
        return ResponseEntity.ok(subscription);
    }

    @Operation(summary ="구독하기 API", description = "사용자가 구독을 시작하고 결제를 준비하는 API")
    @PostMapping("/")
    public ResponseEntity<PaymentResponseDto> initiatePayment(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody PaymentProcessRequestDto paymentDto) {

        // SubscriptionService를 호출하여 구독을 생성하고, PaymentService에서 결제 준비를 진행합니다.
        PaymentResponseDto responseDto = paymentService.processPayment(user, paymentDto);

        return ResponseEntity.ok(responseDto);
    }
}
