package com.project.springbootangularcrud.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductImagesDTO {

    private Long id;
    private String imageName;
    private String imageUrl;

    public ProductImagesDTO(Long id, String imageName, String imageUrl) {
        this.id = id;
        this.imageName = imageName;
        this.imageUrl = imageUrl;
    }
}
