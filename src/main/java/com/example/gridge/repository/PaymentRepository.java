package com.example.gridge.repository;


import com.example.gridge.repository.entity.payment.Payment;
import com.example.gridge.repository.entity.payment.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Payment save(Payment payment);

    Optional<Payment> findByTransactionId(String transactionId);

    Optional<Payment> findTopByUserIdOrderByCreatedAtDesc(Integer id);

    Optional<Payment> findByTransactionIdAndStatus(String merchantUid, PaymentStatus paymentStatus);
}
