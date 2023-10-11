package com.project.ecommerce.controllers;

import com.project.ecommerce.dto.CartItemDTO;
import com.project.ecommerce.exception.ResourceNotFoundException;
import com.project.ecommerce.services.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            return ResponseEntity.ok(cartUpdate);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/cart_count/{username}")
    public ResponseEntity<Integer> cartCount(@PathVariable("username") String userName) {
        try {
            int count = cartService.cartCount(userName);
            return ResponseEntity.ok(count);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/get_cart_items/{username}")
    public ResponseEntity<List<CartItemDTO>> getCartItems(@PathVariable("username") String userName) {
        try {
            List<CartItemDTO> cartItems = cartService.getCartItems(userName);
            return ResponseEntity.ok(cartItems);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "/remove_cart_item/{cartId}/{username}")
    public ResponseEntity<String> removeCartItems(@PathVariable("cartId") Long cartId, @PathVariable("username") String userName) {
        try {
            String response = cartService.removeCartItems(cartId, userName);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
