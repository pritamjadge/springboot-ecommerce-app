package com.project.springbootangularcrud.repository;

import com.project.springbootangularcrud.dto.ProductDTO;
import com.project.springbootangularcrud.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

    @Query("SELECT NEW com.project.springbootangularcrud.dto.ProductDTO(p.productId,p.productName, p.productDescription, p.productQty, p.productPrice) FROM Product p")
    List<ProductDTO> getAllProducts();

}
