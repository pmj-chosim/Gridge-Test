package com.example.gridge.repository.entity.payment;

import com.example.gridge.repository.entity.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "subscription_id", nullable = true)
    private Subscription subscription;

    private Integer paymentAmount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private String transactionId;
    private LocalDateTime createdAt;


    public static Payment create(User user, Integer paymentAmount, PaymentStatus status, String merchantUid) {
        return new Payment(
                null,
                user,
                null,
                paymentAmount,
                status,
                merchantUid,
                LocalDateTime.now()
        );
    }

    // PaymentService의 호출에 맞춰 메서드 이름을 updateImpUid로 변경
    public void updateImpUid(String impUid) {
        this.transactionId = impUid;
    }

    public void updateStatus(PaymentStatus paymentStatus) {
        this.status = paymentStatus;
    }
}