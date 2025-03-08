package com.company.blinkit_Application.repository;

import com.company.blinkit_Application.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
}
