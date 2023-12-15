package com.project.ecommerce.services;

import com.project.ecommerce.dto.CartItemDTO;
import com.project.ecommerce.exception.ResourceNotFoundException;
import com.project.ecommerce.models.Cart;
import com.project.ecommerce.models.Product;
import com.project.ecommerce.models.TransactionDetails;
import com.project.ecommerce.models.User;
import com.project.ecommerce.repository.CartRepo;
import com.project.ecommerce.repository.ProductRepo;
import com.project.ecommerce.repository.UserRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private static final int MAXIMUM_PRODUCT_IN_CART = 6;

    private static final String KEY = "rzp_test_aXITgH9ynkNfEA";
    private static final String SECRET = "wuWQpDvLNQnSKj1PAPssziBC";
    private static final String CURRENCY = "INR";
    private static final String AMOUNT =  "amount";

    private final CartRepo cartRepo;
    private final UserRepository userRepository;
    private final ProductRepo productRepo;

    public CartService(CartRepo cartRepo, UserRepository userRepository, ProductRepo productRepo) {
        this.cartRepo = cartRepo;
        this.userRepository = userRepository;
        this.productRepo = productRepo;
    }

    private CartItemDTO convertToCartItemDTO(Cart cartItem) {
        Product product = cartItem.getProduct();
        String imageUrl = product.getProductImages().isEmpty() ? "" : product.getProductImages().get(0).getImageUrl();

        return CartItemDTO.builder().cartId(cartItem.getCartId()).productId(product.getProductId()).productName(product.getProductName()).productDescription(product.getProductDescription()).productPrice(product.getProductPrice()).productQty(cartItem.getProductQty()).productImageUrl(imageUrl).build();
    }

    public String addToCart(String userName, Long productId, Integer productQty) throws ResourceNotFoundException, IllegalArgumentException {
        User user = userRepository.findByUsername(userName).orElseThrow(() -> new ResourceNotFoundException("User does not exist"));

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
            Product product = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found"));

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
        return getCartByUserId(user.getId()).stream().mapToInt(Cart::getProductQty).sum();
    }

    public List<CartItemDTO> getCartItems(String userName) {
        User user = getUserByUsername(userName);
        Long userId = user.getId();
        return getCartByUserId(userId).stream().map(this::convertToCartItemDTO).toList();
    }

    private User getUserByUsername(String userName) {
        return userRepository.findByUsername(userName).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Transactional
    public String removeCartItems(Long cartId, String userName) {
        User user = getUserByUsername(userName);
        Long userId = user.getId();
        cartRepo.deleteByCartIdAndUserId(cartId, userId);
        return "Item removed successfully";
    }

    private List<Cart> getCartByUserId(Long userId) {
        return cartRepo.findByUser_Id(userId);
    }

    public String updateCartProductQuantity(Long cartId, Integer selectedProductQty) {
        int affectedRow = cartRepo.updateProductQtyByCartId(cartId, selectedProductQty);

        if (affectedRow == 1) {
            return "Product quantity updated successfully.";
        } else {
            throw new ResourceNotFoundException("Cart not found with id: " + cartId);
        }
    }

    public TransactionDetails createTransaction(Double grandAmount) {
        try {
            RazorpayClient client = new RazorpayClient(KEY, SECRET);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put(AMOUNT, grandAmount * 100);
            jsonObject.put("currency", CURRENCY);
            //   jsonObject.put("receipt", "txt_123456");

            Order order = client.orders.create(jsonObject);
            return prepareTransactionDetails(order);
        } catch (RazorpayException e) {
            e.printStackTrace();
        }
        return null;
    }

    private TransactionDetails prepareTransactionDetails(Order order) {
        // Assuming get returns an Object, you may need to cast to the appropriate type
        String orderId = order.get("id").toString(); // Assuming order.get("id") returns an Object
        String currency = order.get("currency").toString(); // Assuming order.get("currency") returns an Object

        // Convert the Integer amount to Double
        Double amount = order.get(AMOUNT) instanceof Integer integer ? integer.doubleValue() : (Double) order.get(AMOUNT);

        return TransactionDetails.builder().orderId(orderId).currency(currency).amount(amount).key(KEY).build();
    }
}
