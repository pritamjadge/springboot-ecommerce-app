package com.project.ecommerce.services;

import com.project.ecommerce.dto.CartItemDTO;
import com.project.ecommerce.dto.ProductDTO;
import com.project.ecommerce.exception.ResourceNotFoundException;
import com.project.ecommerce.models.Cart;
import com.project.ecommerce.models.Product;
import com.project.ecommerce.models.User;
import com.project.ecommerce.repository.CartRepo;
import com.project.ecommerce.repository.ProductRepo;
import com.project.ecommerce.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {

    private static final int MAXIMUM_PRODUCT_IN_CART = 6;

    private final CartRepo cartRepo;
    private final UserRepository userRepository;
    private final ProductRepo productRepo;

    public CartService(CartRepo cartRepo, UserRepository userRepository, ProductRepo productRepo) {
        this.cartRepo = cartRepo;
        this.userRepository = userRepository;
        this.productRepo = productRepo;
    }


    public String addToCart(String userName, Long productId, Integer productQty) throws ResourceNotFoundException, IllegalArgumentException {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new ResourceNotFoundException("User does not exist"));

        if (productQty <= 0) {
            throw new IllegalArgumentException("Invalid product quantity.");
        }

        if (productQty > MAXIMUM_PRODUCT_IN_CART) {
            throw new IllegalArgumentException("You can add a maximum of " + MAXIMUM_PRODUCT_IN_CART + " quantity of this product.");
        }

        Optional<Cart> existingCart = cartRepo.findByUserIdAndProductProductId(user.getId(), productId);

        if (existingCart.isPresent()) {
            Cart cart = existingCart.get();
            int currentQty = cart.getProductQty();

            if (currentQty == MAXIMUM_PRODUCT_IN_CART) {
                throw new IllegalArgumentException("Only 6 quantity of each product allowed to add to cart. You have exceeded the limit.");
            }

            int updatedQty = currentQty + productQty;
            cart.setProductQty(updatedQty);
            cart.setUpdatedAt(new Date());
            cartRepo.save(cart);
        } else {
            Product product = productRepo.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

            Cart cart = new Cart();
            cart.setUser(user);
            cart.setProduct(product);
            cart.setProductQty(productQty);
            cart.setUpdatedAt(new Date());
            cartRepo.save(cart);
        }

        return "The product added to the cart successfully.";
    }

    public Integer cartCount(String userName) throws ResourceNotFoundException {
        User user = getUserByUsername(userName);
        return getCartByUserId(user.getId())
                .stream()
                .mapToInt(Cart::getProductQty)
                .sum();
    }

    public List<CartItemDTO> getCartItems(String userName) {
        User user = getUserByUsername(userName);
        Long userId = user.getId();
        return getCartByUserId(userId)
                .stream()
                .map(this::convertToCartItemDTO)
                .collect(Collectors.toList());
    }

    private User getUserByUsername(String userName) {
        return userRepository.findByUsername(userName)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private CartItemDTO convertToCartItemDTO(Cart cartItem) {
        Product product = cartItem.getProduct();
        String imageUrl = product.getProductImages().isEmpty() ? "" : product.getProductImages().get(0).getImageUrl();

        return CartItemDTO.builder()
                .cartId(cartItem.getCartId())
                .productId(product.getProductId())
                .productName(product.getProductName())
                .productDescription(product.getProductDescription())
                .productPrice(product.getProductPrice())
                .productQty(cartItem.getProductQty())
                .productImageUrl(imageUrl)
                .build();
    }


    private List<Cart> getCartByUserId(Long userId) {
        return cartRepo.findByUser_Id(userId);
    }

}
