package com.example.gridge.repository;

import com.example.gridge.repository.entity.payment.Subscription;
import com.example.gridge.repository.entity.payment.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {
    Optional<Subscription> findByUserId(Integer userId);

    boolean existsByUserId(Integer id);

}
