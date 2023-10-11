package com.example.gadgetgalaxy.controller;

import com.example.gadgetgalaxy.entity.Order;
import com.example.gadgetgalaxy.service.OrderService;
import com.example.gadgetgalaxy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {

    private final OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String listOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "admin/orders/list";
    }

    @GetMapping("/{id}")
    public String viewOrder(@PathVariable Long id, Model model) {
        Order order = orderService.getOrderByIdWithCartItems(id);
        model.addAttribute("order", order);
        return "admin/orders/view";
    }


    @GetMapping("/edit/{id}")
    public String editOrder(@PathVariable Long id, Model model) {
        model.addAttribute("order", orderService.getOrderById(id));

        // Добавляем список всех пользователей в модель
        model.addAttribute("users", userService.getAllUsers());  // Предполагаемое имя метода для получения всех пользователей

        return "admin/orders/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateOrder(@PathVariable Long id, @ModelAttribute Order order) {
        orderService.saveOrder(order);
        return "redirect:/admin/orders/" + id;
    }

    @GetMapping("/delete/{id}")
    public String deleteOrder(@PathVariable Long id) {
        orderService.deleteById(id);
        return "redirect:/admin/orders";
    }
}
