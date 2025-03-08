package com.company.blinkit_Application.service;


import com.company.blinkit_Application.dtos.OrderDto;
import com.company.blinkit_Application.entities.Order;
import com.company.blinkit_Application.enums.OrderStatus;
import com.company.blinkit_Application.exceptions.ResourceNotFoundException;
import com.company.blinkit_Application.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ResourceClosedException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    public List<OrderDto> getOrdersByCustomerId(Long customerId){
        log.info("Fetching orders for customer ID: {}",customerId);
        return orderRepository.findByCustomerId(customerId).stream()
                .map(order->modelMapper.map(order, OrderDto.class))
                .collect(Collectors.toList());
    }

    public OrderDto createOrder(OrderDto orderDto){
        log.info("Creating new order for customer ID: {}",orderDto.getCustomerName());
        Order order = modelMapper.map(orderDto, Order.class);
        order = orderRepository.save(order);
        log.info("Order created successfully with ID: {}",order.getId());
        return modelMapper.map(order, OrderDto.class);
    }

    public OrderDto updateOrderStatus(Long id, String status){
        log.info("Updating order status for ID: {}",id);
        Order order = orderRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Order not found"));

        order.setStatus(OrderStatus.valueOf(status));
        orderRepository.save(order);
        log.info("Order status updated successfully to {}",status);
        return modelMapper.map(order,OrderDto.class);
    }
    public void deleteOrder(Long id){
        log.info("Deleting order with ID: {}",id);
        Order order = orderRepository.findById(id)
                .orElseThrow(()->new ResourceClosedException("Order not found"));
        orderRepository.delete(order);
        log.info("Order deleted successfully");
    }
}
