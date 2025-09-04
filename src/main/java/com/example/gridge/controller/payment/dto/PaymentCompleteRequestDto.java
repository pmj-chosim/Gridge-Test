package com.example.gridge.controller.payment.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Schema(description = "결제 완료 요청 DTO")
public class PaymentCompleteRequestDto {
    @Schema(description = "포트원에서 발급한 고유 결제 ID", example = "imp_1234567890", required = true)
    private String impUid;

    @Schema(description = "백엔드에서 생성한 고유 주문 번호", example = "your_unique_order_id", required = true)
    private String merchantUid;
}