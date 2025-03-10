package com.company.blinkit_Application.service;


import com.company.blinkit_Application.dtos.CategoryDto;
import com.company.blinkit_Application.entities.Category;
import com.company.blinkit_Application.exceptions.ResourceNotFoundException;
import com.company.blinkit_Application.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService  {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    // create a new category
    public CategoryDto createCategory(CategoryDto categoryDto){
        Category category = modelMapper.map(categoryDto, Category.class);
        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDto.class);
    }

    // get Category by Id
    public CategoryDto getCategoryById(Long id){
        Category category = categoryRepository.findById(id)
                .orElseThrow(()->
                    new ResourceNotFoundException("Category with ID "+ id + "not found ."));
    return modelMapper.map(category, CategoryDto.class);
    }

    // Retrieve all categories
    public List<CategoryDto> getAllCategories(){
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(category->modelMapper.map(category, CategoryDto.class))
                .collect(Collectors.toList());
    }

    // update an existing category
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto){
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Category witn ID "+ id + "not found"));
        modelMapper.map(categoryDto, existingCategory);
        Category updatedCategory = categoryRepository.save(existingCategory);
        return modelMapper.map(updatedCategory, CategoryDto.class);
    }

    // delete a category
    public void deleteCategory(Long id){
        Category category = categoryRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Category with ID "+id+"not found."));
        categoryRepository.delete(category);
    }
}
