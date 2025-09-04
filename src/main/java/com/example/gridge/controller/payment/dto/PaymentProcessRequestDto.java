package com.example.gridge.controller.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(name = "포트원 결제 요청 전 유효성 검증 DTO", description = "결제 요청에 필요한 정보를 담고 있는 DTO")
public class PaymentProcessRequestDto {

    @Schema(description = "고유 주문 번호", example = "1234", required = true)
    private String merchantUid;

    @Schema(description = "결제 금액", example = "9900", required = true)
    private Integer amount;
}