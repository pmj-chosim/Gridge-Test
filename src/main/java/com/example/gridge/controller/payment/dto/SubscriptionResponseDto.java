package com.example.gridge.controller.payment.dto;


import com.example.gridge.repository.entity.payment.Subscription;
import com.example.gridge.repository.entity.payment.SubscriptionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "구독 정보 응답 DTO")
public class SubscriptionResponseDto {
    @Schema(description = "구독 ID", example = "1")
    private Integer subscriptionId;

    @Schema(description = "사용자 ID", example = "101")
    private Integer userId;

    @Schema(description = "구독 상태", example = "active")
    private SubscriptionStatus status;

    @Schema(description = "구독 시작 날짜", example = "2024-09-01T00:00:00Z")
    private String startDate;

    @Schema(description = "구독 종료 날짜", example = "2025-08-31T00:00:00Z")
    private String endDate;

    public static SubscriptionResponseDto from(Subscription subscription) {
        return new SubscriptionResponseDto(
                subscription.getId(),
                subscription.getUser().getId(),
                subscription.getType(),
                subscription.getStartSubscription().toString(),
                subscription.getEndSubscription().toString()
        );
    }

}