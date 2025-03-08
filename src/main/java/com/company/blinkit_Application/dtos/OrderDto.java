package com.company.blinkit_Application.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {

    private Long id;
    private String customerName;
    private String status;
    private List<OrderItemDto> orderItems;

}
