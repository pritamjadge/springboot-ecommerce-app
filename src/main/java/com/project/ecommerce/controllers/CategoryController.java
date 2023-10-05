package com.project.ecommerce.controllers;

import com.project.ecommerce.dto.CategoryDTO;
import com.project.ecommerce.models.Category;
import com.project.ecommerce.repository.CategoryRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/category")
public class CategoryController {

    final CategoryRepo categoryRepo;

    public CategoryController(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    @PostMapping("/add_category")
    public ResponseEntity<String> addCategory(@RequestBody Category category) {

        try {
            categoryRepo.save(category);
            return new ResponseEntity<>("Category added successfully..!", HttpStatus.CREATED);
        } catch (Exception e) {

            return new ResponseEntity<>("Error Adding Category " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/get_categories")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<Category> categories = categoryRepo.findAll();

        List<CategoryDTO> categoryList = categories.stream().map(category -> new CategoryDTO(category.getCategoryId(), category.getCategoryName())).collect(Collectors.toList());
        System.out.println(categoryList);
        return new ResponseEntity<>(categoryList, HttpStatus.OK);
    }
}