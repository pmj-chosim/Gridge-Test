package com.example.gridge.controller.payment.dto;

import com.example.gridge.repository.entity.payment.Payment;
import com.example.gridge.repository.entity.payment.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PaymentResponseDto {
    @Schema(description = "결제 ID", example = "1")
    private Integer paymentId;

    @Schema(description = "사용자 ID", example = "101")
    private Integer userId;

    @Schema(description = "결제 금액", example = "9900")
    private Integer paymentAmount;

    @Schema(description = "결제 상태", example = "completed")
    private PaymentStatus status;

    @Schema(description = "거래 ID", example = "imp_1234567890")
    private String transactionId;

    @Schema(description = "생성일자", example = "2024-09-01T00:00:00Z")
    private String createdAt;

    public static PaymentResponseDto from(Payment payment) {
        return new PaymentResponseDto(
                payment.getId(),
                payment.getUser().getId(),
                payment.getPaymentAmount(),
                payment.getStatus(),
                payment.getTransactionId(),
                payment.getCreatedAt().toString()
        );
    }
}
