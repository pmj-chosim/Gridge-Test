package com.example.gridge.repository.entity.payment;


import com.example.gridge.repository.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
@AllArgsConstructor
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


    public static Payment create(User user, Integer paymentAmount, PaymentStatus status, String transactionId) {
        return new Payment(
        null, user, null, paymentAmount, status, transactionId, LocalDateTime.now()
         );
    }

}
