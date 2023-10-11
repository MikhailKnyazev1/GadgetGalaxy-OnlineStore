package com.example.gadgetgalaxy.service;

import com.example.gadgetgalaxy.entity.CartItem;
import com.example.gadgetgalaxy.entity.Product;
import com.example.gadgetgalaxy.rdb.repository.CartItemRepository;
import com.example.gadgetgalaxy.rdb.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CartItemService(CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    public void removeItem(Long id) {
        Optional<CartItem> cartItemOptional = cartItemRepository.findById(id);

        if(cartItemOptional.isPresent()) {
            CartItem cartItem = cartItemOptional.get();
            int quantity = cartItem.getQuantity();

            // Если товара в корзине более одного, уменьшаем его количество на единицу
            if (quantity > 1) {
                cartItem.setQuantity(quantity - 1);
                cartItemRepository.save(cartItem);
                System.out.println("Quantity of cart item with ID: " + id + " reduced by 1");
            }
            // Если товара остался только один, удаляем его из корзины
            else {
                cartItemRepository.deleteById(id);
                System.out.println("Cart item with ID: " + id + " deleted successfully");
            }
        } else {
            System.out.println("Cart item not found with ID: " + id);
        }
    }


    public List<CartItem> getAllCartItems() {
        return cartItemRepository.findAll();
    }

    public Optional<CartItem> getCartItemById(Long id) {
        return cartItemRepository.findById(id);
    }

    public CartItem saveCartItem(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }

    public void addProductToCart(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        CartItem cartItem = cartItemRepository.findByProductId(productId)
                .orElse(new CartItem());

        cartItem.setProduct(product);
        cartItem.setQuantity(cartItem.getQuantity() + 1);

        cartItemRepository.save(cartItem);
    }

    public void clearCart() {
        cartItemRepository.deleteAll();
    }

    public BigDecimal calculateTotalCartValue() {
        List<CartItem> cartItems = getAllCartItems();
        return cartItems.stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }



}
