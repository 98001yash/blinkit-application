package com.company.blinkit_Application.dtos;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryDto {

    private Long id;
    private String orderId;
    private String deliveryPartner;
    private String status;
}
