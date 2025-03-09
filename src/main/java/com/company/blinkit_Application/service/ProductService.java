package com.company.blinkit_Application.service;


import com.company.blinkit_Application.dtos.ProductDto;
import com.company.blinkit_Application.entities.Product;
import com.company.blinkit_Application.exceptions.ResourceNotFoundException;
import com.company.blinkit_Application.repository.ProductRepository;
import jakarta.transaction.Transactional;
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



    @Transactional
    public ProductDto addProduct(ProductDto productDto){
        log.info("Adding new product :{}",productDto.getName());
        Product product = modelMapper.map(productDto, Product.class);
        Product savedProduct = productRepository.save(product);
        log.info("Product saved successfully with ID: {}",savedProduct.getId());
        return modelMapper.map(savedProduct, ProductDto.class);
    }

    // get Product details by Id
    public List<ProductDto> getAllProducts(){
        log.info("Fetching all products..");
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(product->modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());
    }

    // update product details
    @Transactional
    public ProductDto updateProduct(Long productId, ProductDto productDto){
        log.info("Updating product with ID: {}",productId);
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product not found with ID: "+productId));

        existingProduct.setName(productDto.getName());
        existingProduct.setDescription(productDto.getDescription());
        existingProduct.setPrice(productDto.getPrice());
        existingProduct.setStockQuantity(productDto.getStockQuantity());

        Product updatedProduct = productRepository.save(existingProduct);
        log.info("Product updated successfully with ID: {}",updatedProduct.getId());
        return modelMapper.map(updatedProduct, ProductDto.class);
    }


    @Transactional
    public void deleteProduct(Long productId){
        log.info("Deleting product with ID: {}",productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product not found with ID: "+productId));

        productRepository.delete(product);
        log.info("Product deleted successfully with ID: {}",productId);
    }

}
