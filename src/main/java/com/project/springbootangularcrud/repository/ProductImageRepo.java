package com.project.springbootangularcrud.repository;

import com.project.springbootangularcrud.dto.ProductImagesDTO;
import com.project.springbootangularcrud.models.ProductImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductImageRepo extends JpaRepository<ProductImages, Long> {

    @Query("SELECT NEW com.project.springbootangularcrud.dto.ProductImagesDTO(pi.id, pi.imageUrl, pi.imageName) " +
            "FROM ProductImages pi WHERE pi.product.productId = :productId")
    List<ProductImagesDTO> findAllByProductId(@Param("productId") Long productId);
}
