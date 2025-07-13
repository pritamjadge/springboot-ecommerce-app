package com.project.ecommerce.models;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

public class OrderDetails {

    private Long orderDetailsId;

    @OneToOne(targetEntity = Product.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "product_id")
    private Product product;

    @OneToOne(targetEntity = Order.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "order_id")
    private OrderDetails orderDetails;

    private Integer productQty;

    private Double totalAmount;
}
