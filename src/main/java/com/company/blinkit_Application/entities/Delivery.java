package com.company.blinkit_Application.entities;


import com.company.blinkit_Application.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "deliveries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id",nullable= false)
    private Order order;

    @ManyToOne
    @JoinColumn(name  ="delivery_partner_id")
    private User deliveryPartner;

    private LocalDateTime estimatedDeliveryTime;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;
}
