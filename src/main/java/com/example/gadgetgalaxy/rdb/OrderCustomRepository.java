package com.example.gadgetgalaxy.rdb;

import com.example.gadgetgalaxy.entity.Order;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository("orderCustomRepository")
public class OrderCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Order> findAll() {
        return entityManager.createQuery("from Order", Order.class).getResultList();
    }

    public Order findById(Long id) {
        return entityManager.find(Order.class, id);
    }

    @Transactional
    public Order save(Order order) {
        if (order.getId() == null) {
            entityManager.persist(order);
            return order;
        } else {
            return entityManager.merge(order);
        }
    }

    @Transactional
    public void deleteById(Long id) {
        Order order = findById(id);
        if (order != null) {
            entityManager.remove(order);
        }
    }
}
