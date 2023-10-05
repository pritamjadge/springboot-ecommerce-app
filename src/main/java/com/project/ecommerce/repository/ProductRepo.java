package com.project.ecommerce.repository;

import com.project.ecommerce.dto.ProductDTO;
import com.project.ecommerce.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

    @Query("SELECT NEW com.project.ecommerce.dto.ProductDTO(p.productId,p.productName, p.productDescription, p.productQty, p.productPrice) FROM Product p")
    List<ProductDTO> getAllProducts();

    Page<Product> findByProductNameContaining(String productName, Pageable paging);

    Page<Product> findByCategoryCategoryId(Long categoryId, Pageable paging);

    Page<Product> findByProductNameContainingAndCategoryCategoryId(String productName, Long categoryId, Pageable pageable);

/*
    @Query("SELECT p FROM Product p WHERE LOWER(p.productName) LIKE %:productName%")
    Page<Product> findByProductNameContainingIgnoreCase(@Param("productName") String productName, Pageable pageable);
*/

}
