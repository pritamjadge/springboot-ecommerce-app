package com.project.ecommerce.repository;
import com.project.ecommerce.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepo extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserIdAndProductProductId(Long userId, Long productId);

    List<Cart> findByUser_Id(Long userId);

}
