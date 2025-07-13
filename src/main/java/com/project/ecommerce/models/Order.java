package com.project.ecommerce.models;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

public class Order {

    private Long orderId;

    private String transactionId;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @OneToOne(targetEntity = ShippingDetails.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "shipping_id")
    private ShippingDetails shippingDetails;

}
