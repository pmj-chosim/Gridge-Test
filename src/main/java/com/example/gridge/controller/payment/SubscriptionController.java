package com.example.gridge.controller.payment;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
@Tag(name = "구독 API", description = "구독 관련 API")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @Operation(summary = "내 구독 정보 조회 API", description = "현재 로그인한 사용자의 구독 정보를 조회하는 API")
    @GetMapping("/me")
    public ResponseEntity<SubscriptionResponseDto> getMySubscription(@AuthenticationPrincipal User user) {
        SubscriptionResponseDto subscription = subscriptionService.getSubscriptionByUserId(user.getId());
        return ResponseEntity.ok(subscription);
    }
}
