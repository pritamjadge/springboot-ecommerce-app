package com.project.ecommerce.controllers;

import com.project.ecommerce.exception.ResourceNotFoundException;
import com.project.ecommerce.services.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add_to_cart/{user_name}/{product_id}")
    public ResponseEntity<String> productAddToCart(
            @PathVariable("user_name") String userName,
            @PathVariable("product_id") Long productId,
            @RequestParam(value = "product_qty", defaultValue = "1", required = false) Integer productQty) {

        try {
            String cartUpdate = cartService.addToCart(userName, productId, productQty);
            System.out.println(cartUpdate);
            return ResponseEntity.ok(cartUpdate);
        } catch (ResourceNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/cart_count/{username}")
    public ResponseEntity<?> cartCount(@PathVariable("username") String userName) {
        try {
            return ResponseEntity.ok(cartService.cartCount(userName));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
