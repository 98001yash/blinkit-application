package com.company.blinkit_Application.service;

import com.company.blinkit_Application.dtos.OrderDto;
import com.company.blinkit_Application.dtos.OrderItemDto;
import com.company.blinkit_Application.entities.Order;
import com.company.blinkit_Application.entities.OrderItem;
import com.company.blinkit_Application.entities.User;
import com.company.blinkit_Application.enums.OrderStatus;
import com.company.blinkit_Application.exceptions.ResourceNotFoundException;
import com.company.blinkit_Application.repository.OrderRepository;
import com.company.blinkit_Application.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    /**
     * Place a new order.
     */
    @Transactional
    public OrderDto placeOrder(OrderDto orderDto) {
        log.info("Placing an order for User ID: {}", orderDto.getUserId());

        User user = userRepository.findById(orderDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + orderDto.getUserId()));

        // Convert OrderItemDto to OrderItem entity
        List<OrderItem> orderItems = orderDto.getOrderItems().stream()
                .map(orderItemDto -> modelMapper.map(orderItemDto, OrderItem.class))
                .collect(Collectors.toList());

        Order order = Order.builder()
                .user(user)
                .customerName(orderDto.getCustomerName())
                .status(OrderStatus.valueOf("PENDING"))
                .orderItems(orderItems)
                .build();

        // Set order reference in order items
        orderItems.forEach(item -> item.setOrder(order));

        Order savedOrder = orderRepository.save(order);
        log.info("Order placed successfully with ID: {}", savedOrder.getId());

        return modelMapper.map(savedOrder, OrderDto.class);
    }

    /**
     * Get order details by order ID.
     */
    public OrderDto getOrderById(Long orderId) {
        log.info("Fetching order details for ID: {}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        return modelMapper.map(order, OrderDto.class);
    }

    /**
     * Get all orders for a user.
     */
    public List<OrderDto> getOrdersByUserId(Long userId) {
        log.info("Fetching all orders for user ID: {}", userId);
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
                .map(order -> modelMapper.map(order, OrderDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Update order status.
     */
    @Transactional
    public OrderDto updateOrderStatus(Long orderId, String status) {
        log.info("Updating order status for order ID: {} to {}", orderId, status);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        order.setStatus(OrderStatus.valueOf(status));
        Order updatedOrder = orderRepository.save(order);
        log.info("Order status updated successfully for order ID: {}", orderId);

        return modelMapper.map(updatedOrder, OrderDto.class);
    }

    /**
     * Cancel an order.
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        log.info("Canceling order with ID: {}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        order.setStatus(OrderStatus.valueOf("CANCELED"));
        orderRepository.save(order);
        log.info("Order canceled successfully for ID: {}", orderId);
    }
}
