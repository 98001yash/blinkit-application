package com.company.blinkit_Application.entities;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Integer stockQuantity;

    @ManyToOne
    @JoinColumn(name = "category_id",nullable = false)
    private Category category;
}
