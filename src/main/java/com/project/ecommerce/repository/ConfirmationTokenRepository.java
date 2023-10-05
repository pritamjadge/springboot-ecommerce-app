package com.project.ecommerce.repository;

import com.project.ecommerce.models.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken,Long> {

    ConfirmationToken findByConfirmationToken(String confirmationToken);
}
