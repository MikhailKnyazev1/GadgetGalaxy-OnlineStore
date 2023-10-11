package com.example.gadgetgalaxy.rdb.repository;

import com.example.gadgetgalaxy.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByProductId(Long productId);
}
