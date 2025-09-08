package com.example.gridge.repository.entity.payment;

import com.example.gridge.repository.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Subscription {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "subscription")
    private List<Payment> payments = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus type;

    private LocalDate startSubscription;
    private LocalDate endSubscription;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    // Payment를 인자로 받지 않는 새로운 create 메서드
    public static Subscription create(User user, SubscriptionStatus type, Integer durationInMonths) {
        return new Subscription(
                null,
                user,
                new ArrayList<>(),
                type,
                LocalDate.now(),
                LocalDate.now().plusMonths(durationInMonths),
                LocalDate.now(),
                LocalDate.now()
        );
    }
    public void addPayment(Payment payment) {
        this.payments.add(payment);
        payment.setSubscription(this);
    }



}
