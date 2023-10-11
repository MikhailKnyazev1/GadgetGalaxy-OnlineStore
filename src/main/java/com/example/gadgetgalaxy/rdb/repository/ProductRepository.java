package com.example.gadgetgalaxy.rdb.repository;

import com.example.gadgetgalaxy.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Все необходимые методы, такие как findAll, findById и другие, уже включены в JpaRepository.
}
