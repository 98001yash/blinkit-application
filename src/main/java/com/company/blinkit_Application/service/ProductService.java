package com.company.blinkit_Application.service;


import com.company.blinkit_Application.dtos.ProductDto;
import com.company.blinkit_Application.entities.Product;
import com.company.blinkit_Application.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public List<ProductDto> getAllProducts(){
        log.info("Fetching all products...");
        return productRepository.findAll().stream()
                .map(product->modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());
    }

    public ProductDto createProduct(ProductDto productDto){
        log.info("Creating new Product: {}",productDto.getName());
        Product product  = modelMapper.map(productDto, Product.class);
        log.info("Product created successfully with ID: {}",product.getId());
        return modelMapper.map(product, ProductDto.class);
    }
}
