package com.project.ecommerce.dto;


import lombok.*;

@Getter
@Setter
@Builder
public class CategoryDTO {

    private Long categoryId;
    private String categoryName;

    public CategoryDTO(){

    }

    public CategoryDTO(Long categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }
}
