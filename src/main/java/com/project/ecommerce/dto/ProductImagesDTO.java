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
    // private String imageName;

    public ProductImagesDTO(Long id, String imageUrl) {
        this.id = id;
        this.imageUrl = imageUrl;
        // this.imageName = imageName;
    }

    public ProductImagesDTO() {
    }
}
