package com.example.gridge.controller.payment;

import com.example.gridge.controller.payment.dto.PaymentCancelRequestDto;
import com.example.gridge.controller.payment.dto.PaymentCompleteRequestDto;
import com.example.gridge.controller.payment.dto.PaymentProcessRequestDto;
import com.example.gridge.controller.payment.dto.PaymentResponseDto;
import com.example.gridge.repository.entity.user.User;
import com.example.gridge.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "결제 API", description = "결제 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
@Getter
public class PaymentController {
    private final PaymentService paymentService;

    @Operation(summary = "내 결제 정보 조회 API", description = "현재 로그인한 사용자의 결제 정보를 조회하는 API")
    @GetMapping("/me")
    public ResponseEntity<PaymentResponseDto> getMyPayments(@AuthenticationPrincipal User user) {
        PaymentResponseDto payment = paymentService.getPaymentByUserId(user.getId());
        return ResponseEntity.ok(payment);
    }

    @Operation(summary = "결제 준비 API", description = "포트원 결제 요청에 앞서 백엔드에서 금액을 검증하고, 고유한 주문 번호(merchantUid)를 생성기 위한 API")
    @PostMapping("/prepare")
    public ResponseEntity<PaymentResponseDto> processPayment(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody PaymentProcessRequestDto request) {
        PaymentResponseDto payment = paymentService.processPayment(user, request);
        return ResponseEntity.ok(payment);
    }

    @Operation(summary="결제 완료 API", description = "포트원 결제가 성공적으로 완료된 후, 결제 완료를 처리하는 API")
    @PostMapping("/complete")
    public ResponseEntity<PaymentResponseDto> completePayment(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody PaymentCompleteRequestDto request) {
        PaymentResponseDto pay=paymentService.completePayment(user, request);
        return ResponseEntity.ok(pay);
    }

    @Operation(summary = "결제 취소 API", description = "결제가 완료된 주문을 취소하거나 부분 취소하는 API입니다.")
    @PostMapping("/cancel")
    public ResponseEntity<String> cancelPayment(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody PaymentCancelRequestDto request) {
        paymentService.cancelPayment(user, request);
        return ResponseEntity.ok("Payment cancelled successfully.");
    }
}
