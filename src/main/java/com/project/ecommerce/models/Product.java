package com.project.ecommerce.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private String productName;
    private String productDescription;
    private Integer productQty;
    private Double productPrice;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonIgnoreProperties("products")
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ProductImages> productImages;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP(0)")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp // This annotation automatically sets the timestamp on creation
    private Date createdAt;

    private String createdBy;

    public Product(Long productId, String productName, String productDescription, Integer productQty, Double productPrice, List<ProductImages> productImages) {
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productQty = productQty;
        this.productPrice = productPrice;
        this.productImages = productImages;
    }


    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productDescription='" + productDescription + '\'' +
                ", productQty=" + productQty +
                ", productPrice=" + productPrice +
                ", category=" + category +
                ", productImages=" + productImages +
                ", createdAt=" + createdAt +
                ", createdBy='" + createdBy + '\'' +
                '}';
    }
}
