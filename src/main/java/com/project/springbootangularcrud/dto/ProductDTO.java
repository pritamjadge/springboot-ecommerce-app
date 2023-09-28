package com.project.springbootangularcrud.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ProductDTO {
    private Long productId;
    private String productName;
    private String productDescription;
    private Integer productQty;
    private Double productPrice;
    private List<ProductImagesDTO> productImages;

    public ProductDTO(Long productId, String productName, String productDescription, Integer productQty, Double productPrice, List<ProductImagesDTO> productImages) {
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productQty = productQty;
        this.productPrice = productPrice;
        this.productImages = productImages;
    }
}