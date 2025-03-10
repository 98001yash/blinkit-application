package com.company.blinkit_Application.service;


import com.company.blinkit_Application.dtos.DeliveryDto;
import com.company.blinkit_Application.entities.Delivery;
import com.company.blinkit_Application.entities.Order;
import com.company.blinkit_Application.entities.User;
import com.company.blinkit_Application.enums.DeliveryStatus;
import com.company.blinkit_Application.exceptions.BadRequestException;
import com.company.blinkit_Application.exceptions.ResourceNotFoundException;
import com.company.blinkit_Application.repository.DeliveryRepository;
import com.company.blinkit_Application.repository.OrderRepository;
import com.company.blinkit_Application.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public DeliveryDto assignDelivery(Long orderId, Long partnerId, LocalDateTime estimatedTime){
        log.info("Assigning delivery for order ID:{}",orderId);

        // find the order
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()->{
                    log.error("Order with ID {} not found",orderId);
                    return new BadRequestException("Order not found");
                });

        // find the delivery partner
        User deliveryPartner = userRepository.findById(partnerId)
                .orElseThrow(()->{
                    log.error("Delivery partner with ID: {} not found",partnerId);
                    return new ResourceNotFoundException("Delivery partner not found");
                });

        // create and save the delivery object
        Delivery delivery = Delivery.builder()
                .order(order)
                .deliveryPartner(deliveryPartner)
                .estimatedDeliveryTime(estimatedTime)
                .status(DeliveryStatus.ASSIGNED)
                .build();

        delivery = deliveryRepository.save(delivery);
        log.info("Delivery assigned successfully for order ID:  {} to partner ID:{}", orderId, partnerId);
        return modelMapper.map(delivery, DeliveryDto.class);
    }

    // update the delivery status
    public DeliveryDto updateDeliveryStatus(Long deliveryId, DeliveryStatus status){
        log.info("Updating status for delivery ID: {} to {}",deliveryId, status);

        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(()->{
                    log.error("Delivery with ID {} not found",deliveryId);
                    return new ResourceNotFoundException("Delivery Not found");
                });

        delivery.setStatus(status);

        if(status==DeliveryStatus.DELIVERED){
            delivery.setEstimatedDeliveryTime(LocalDateTime.now());
        }

        delivery = deliveryRepository.save(delivery);
        log.info("Delivery status updated successfully for delivery ID: {} to status: {}",deliveryId, status);
        return modelMapper.map(delivery, DeliveryDto.class);
    }
}
