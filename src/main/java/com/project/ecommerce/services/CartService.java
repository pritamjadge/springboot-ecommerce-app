package com.project.ecommerce.services;

import com.project.ecommerce.exception.ResourceNotFoundException;
import com.project.ecommerce.models.Cart;
import com.project.ecommerce.models.Product;
import com.project.ecommerce.models.User;
import com.project.ecommerce.repository.CartRepo;
import com.project.ecommerce.repository.ProductRepo;
import com.project.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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
        Optional<User> userOptional = userRepository.findByUsername(userName);

        return userOptional.map(user -> {
            if (productQty <= 0) {
                return "Invalid product quantity.";
            }

            if (productQty > MAXIMUM_PRODUCT_IN_CART) {
                return "You can add a maximum of " + MAXIMUM_PRODUCT_IN_CART + " quantity of this product.";
            }

            Optional<Cart> existingCart = cartRepo.findByUserIdAndProductProductId(user.getId(), productId);

            if (existingCart.isPresent()) {
                Cart cart = existingCart.get();
                int currentQty = cart.getProductQty();

                if (currentQty == MAXIMUM_PRODUCT_IN_CART) {
                    return "Only 6 quantity of each product allow to add to cart. You have exceed the limit";
                }

                int updatedQty = currentQty + productQty;
                cart.setProductQty(updatedQty);
                cart.setUpdatedAt(new Date());
                cartRepo.save(cart);
            } else {
                Product product = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found"));

                Cart cart = new Cart();
                cart.setUser(user);
                cart.setProduct(product);
                cart.setProductQty(productQty);
                cart.setUpdatedAt(new Date());
                cartRepo.save(cart);
            }
            return "The product added to the cart successfully.";
        }).orElseThrow(() -> new ResourceNotFoundException("User does not exist"));
    }

    public Integer cartCount(String userName) throws ResourceNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(userName);

        if (userOptional.isPresent()) {
            Long userId = userOptional.get().getId();
            List<Cart> userCarts = cartRepo.findByUser_Id(userId);

            return userCarts.stream().mapToInt(Cart::getProductQty).sum();

        } else {
            throw new ResourceNotFoundException("User not found");
        }
    }

}
