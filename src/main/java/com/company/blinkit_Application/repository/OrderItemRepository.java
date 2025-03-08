package com.company.blinkit_Application.repository;

import com.company.blinkit_Application.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
}
