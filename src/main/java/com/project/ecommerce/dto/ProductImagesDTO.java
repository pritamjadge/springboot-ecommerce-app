package com.project.ecommerce.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductImagesDTO {

    private Long id;
    private String imageUrl;

    public ProductImagesDTO(Long id, String imageUrl) {
        this.id = id;
        this.imageUrl = imageUrl;
    }

    public ProductImagesDTO() {
    }
}
