package com.example.gadgetgalaxy.service;

import com.example.gadgetgalaxy.entity.CartItem;
import com.example.gadgetgalaxy.entity.Order;
import com.example.gadgetgalaxy.entity.User;
import com.example.gadgetgalaxy.rdb.OrderCustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private CartItemService cartItemService;

    private final OrderCustomRepository orderRepository;

    @Autowired
    public OrderService(OrderCustomRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional // Добавлено для управления транзакциями
    public Order getOrderByIdWithCartItems(Long id) {
        Optional<Order> orderOptional = Optional.ofNullable(orderRepository.findById(id));
        // Инициализировать cartItems, чтобы избежать ошибки lazy initialization
        orderOptional.ifPresent(order -> order.getCartItems().size());
        return orderOptional.orElse(null);
    }

    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    public void deleteOrderById(Long id) {
        orderRepository.deleteById(id);
    }

    @Transactional
    public Order createOrderFromCartItems(User user) {
        List<CartItem> cartItems = cartItemService.getAllCartItems();
        Order order = new Order();
        order.setUser(user);
        order.setCartItems(cartItems);
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus("NEW"); // Например, установить статус заказа как "NEW" или что-то подобное
        order.setTotalPrice(cartItemService.calculateTotalCartValue());
        return saveOrder(order);
    }

    public Order getOrderById(Long id) {
        Optional<Order> orderOptional = Optional.ofNullable(orderRepository.findById(id));
        return orderOptional.orElse(null);
    }

    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }

}
