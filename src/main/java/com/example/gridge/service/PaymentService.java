package com.example.gridge.service;

import com.example.gridge.controller.payment.dto.PaymentCancelRequestDto;
import com.example.gridge.controller.payment.dto.PaymentCompleteRequestDto;
import com.example.gridge.controller.payment.dto.PaymentProcessRequestDto;
import com.example.gridge.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
@RequiredArgsConstructor
@Getter
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;


    public void processPayment(PaymentProcessRequestDto request) {
    }

    public void completePayment(PaymentCompleteRequestDto request){

    }

    public void cancelPayment(@Valid PaymentCancelRequestDto request) {
    }
}
