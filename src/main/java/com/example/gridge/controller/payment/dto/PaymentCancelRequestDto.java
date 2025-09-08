package com.example.gridge.controller.payment.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "결제 취소 요청 DTO")
public class PaymentCancelRequestDto {
    @Schema(description = "백엔드에서 생성한 고유 주문 번호", example = "your_unique_order_id", required = true)
    private String merchantUid;

    @Schema(description = "취소할 금액 (부분 취소 시 사용)", example = "9900")
    private Integer cancelAmount;
}
