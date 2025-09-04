package com.example.gridge.controller.payment;

import com.example.gridge.controller.payment.dto.SubscriptionResponseDto;
import com.example.gridge.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/subscriptions")
@Tag(name = "구독 API", description = "구독 관련 API")
public class SubscriptionController {
    SubscriptionService subscriptionService;

    @Operation(summary = "내 구독 정보 조회 API", description = "현재 로그인한 사용자의 구독 정보를 조회하는 API")
    @GetMapping("/me")
    public ResponseEntity<SubscriptionResponseDto> getMySubscription() {
        // User 정보는 jwt에서 가져오기
        SubscriptionResponseDto subscription =subscriptionService.getSubscriptionByUserId(//jwt에서 userId 가져오기)
        );
        return ResponseEntity.ok(subscription);
    }
}
