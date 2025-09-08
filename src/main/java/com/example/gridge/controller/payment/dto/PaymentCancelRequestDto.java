package com.example.gridge.controller.payment.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "결제 취소 요청 DTO")
public class PaymentCancelRequestDto {
    @Schema(description = "포트원에서 생성한 id", example = "abc123", required = true)
    private String impUid;

    @Schema(description = "취소할 금액 (부분 취소 시 사용)", example = "9900")
    private Integer cancelAmount;
}
