package com.example.gadgetgalaxy.rdb.repository;

import com.example.gadgetgalaxy.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
