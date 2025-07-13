package com.project.ecommerce.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

//@Entity
@Data
public class ShippingDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @NotBlank
    private String fullName;

    private String mobileNumber;

    private String pinCode;

    private String houseNo;

    private String street;

    private String landmark;

    private String city;

    private String state;
}
