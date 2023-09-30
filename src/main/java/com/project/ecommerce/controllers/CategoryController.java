package com.project.ecommerce.controllers;

import com.project.ecommerce.models.Category;
import com.project.ecommerce.repository.CategoryRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}