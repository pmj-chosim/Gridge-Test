package com.example.gridge.repository.entity.payment;

import com.example.gridge.repository.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Entity
@AllArgsConstructor
public class Subscription {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus type;

    private LocalDate startSubscription;
    private LocalDate endSubscription;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    public static Subscription create(User user, SubscriptionStatus type, Integer durationInMonths) {
        return new Subscription(
                null, user, type, LocalDate.now(), LocalDate.now().plusMonths(durationInMonths), LocalDate.now(), LocalDate.now()
        );
    }

}
