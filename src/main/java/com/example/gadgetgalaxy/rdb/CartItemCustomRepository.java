package com.example.gadgetgalaxy.rdb;

import com.example.gadgetgalaxy.entity.CartItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("cartItemCustomRepository")
public class CartItemCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<CartItem> findAll() {
        return entityManager.createQuery("from CartItem", CartItem.class).getResultList();
    }

    public CartItem findById(Long id) {
        return entityManager.find(CartItem.class, id);
    }

    // Новый метод для поиска элемента корзины по идентификатору продукта
    public Optional<CartItem> findByProductId(Long productId) {
        List<CartItem> cartItems = entityManager.createQuery("from CartItem c where c.product.id = :productId", CartItem.class)
                .setParameter("productId", productId)
                .getResultList();

        return cartItems.isEmpty() ? Optional.empty() : Optional.of(cartItems.get(0));
    }

    public CartItem save(CartItem cartItem) {
        if (cartItem.getId() == null) {
            System.out.println("Persisting new cart item.");
            entityManager.persist(cartItem);
            System.out.println("Persisted new cart item with ID: " + cartItem.getId());
            return cartItem;
        } else {
            System.out.println("Merging existing cart item with ID: " + cartItem.getId());
            CartItem mergedCartItem = entityManager.merge(cartItem);
            System.out.println("Merged cart item with ID: " + mergedCartItem.getId());
            return mergedCartItem;
        }
    }


    public void deleteById(Long id) {
        CartItem cartItem = findById(id);
        if (cartItem != null) {
            entityManager.remove(cartItem);
        }
    }
}
