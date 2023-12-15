package com.project.ecommerce.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class TransactionDetails {

    private String orderId;
    private String currency;
    private Double amount;
    private String key;
}
