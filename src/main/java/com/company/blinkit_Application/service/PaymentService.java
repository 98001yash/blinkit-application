package com.company.blinkit_Application.service;


import com.company.blinkit_Application.entities.Order;
import com.company.blinkit_Application.entities.Payment;
import com.company.blinkit_Application.enums.PaymentMethod;
import com.company.blinkit_Application.enums.PaymentStatus;
import com.company.blinkit_Application.exceptions.ResourceNotFoundException;
import com.company.blinkit_Application.repository.OrderRepository;
import com.company.blinkit_Application.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ResourceClosedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public Payment processPayment(Long orderId, Double amount, PaymentMethod paymentMethod){
        log.info("Processing payment for order ID: {}",orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(()->{
                    log.error("Order with ID {} not found",orderId);
                    return new ResourceClosedException("Order not found");
                });

        // validate Payment amount
        if(!order.getTotalAmount().equals(amount)){
            log.error("Payment amount {} does not match order total {}",amount, order.getTotalAmount());
            throw new ResourceNotFoundException("Invalid payment amount");
        }
        // create and save the payment
        Payment payment = Payment.builder()
                .order(order)
                .amount(amount)
                .paymentMethod(paymentMethod)
                .status(PaymentStatus.PENDING)
                .paymentDate(LocalDateTime.now())
                .build();

        payment = paymentRepository.save(payment);
        log.info("Payment processes successfully for order ID: {}",orderId);
        return payment;
    }

    // update Payment status
    public Payment updatePaymentStatus(Long paymentId, PaymentStatus status){
        log.info("Updating status for payment ID: {} to {}",paymentId, status);

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(()->{
                    log.error("Payment with ID {} not found",paymentId);
                    return new ResourceNotFoundException("Payment not found");
                });

        payment.setStatus(status);
        payment = paymentRepository.save(payment);
        log.info("Payment status updated successfully for payment ID: {}",paymentId);
        return payment;
    }

    // retrieve payment Details
    public Payment getPaymentDetails(Long paymentId){
        log.info("Retrieving details for payment ID: {}",paymentId);

        return paymentRepository.findById(paymentId)
                .orElseThrow(()->{
                    log.error("Payment with ID {} not found",paymentId);
                    return new ResourceNotFoundException("Payment not found");
                });
    }
}
