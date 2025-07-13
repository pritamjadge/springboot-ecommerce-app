package com.project.ecommerce.repository;

import com.project.ecommerce.models.Cart;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CartRepo extends JpaRepository<Cart, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Cart> findByUserIdAndProductProductId(Long userId, Long productId);

    List<Cart> findByUser_Id(Long userId);

    void deleteByCartIdAndUserId(Long cartId, Long userId);

    @Transactional
    @Modifying
    @Query("UPDATE Cart c SET c.productQty = :selectedProductQty, c.updatedAt = current_timestamp WHERE c.cartId = :cartId")
    int updateProductQtyByCartId(@Param("cartId") Long cartId, @Param("selectedProductQty") Integer selectedProductQty);
}
