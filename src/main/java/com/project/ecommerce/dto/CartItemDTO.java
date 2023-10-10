package com.project.ecommerce.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CartItemDTO {
    private Long cartId;
    private Long productId;
    private String productName;
    private Double productPrice;
    private String productDescription;
    private String productImageUrl;
    private Integer productQty;

    // No-argument constructor is recommended if you're using Lombok's builder
    public CartItemDTO() {
    }

    // Constructor to match the order of fields in the class
    public CartItemDTO(Long cartId, Long productId, String productName, Double productPrice, String productDescription, String productImageUrl, Integer productQty) {
        this.cartId = cartId;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productDescription = productDescription;
        this.productImageUrl = productImageUrl;
        this.productQty = productQty;
    }

    @Override
    public String toString() {
        return "CartItemDTO{" +
                "cartId=" + cartId +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productPrice=" + productPrice +
                ", productDescription='" + productDescription + '\'' +
                ", productImageUrl='" + productImageUrl + '\'' +
                ", productQty=" + productQty +
                '}';
    }
}
