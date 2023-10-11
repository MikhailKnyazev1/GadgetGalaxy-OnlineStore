package com.example.gadgetgalaxy.controller;

import com.example.gadgetgalaxy.entity.Product;
import com.example.gadgetgalaxy.entity.User;
import com.example.gadgetgalaxy.service.ProductService;
import com.example.gadgetgalaxy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MainController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @GetMapping("")
    public String index(Model model, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 6); // 6 products per page
        Page<Product> productPage = productService.getProducts(pageable);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());

        // Получите текущего авторизованного пользователя
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        User currentUser = userService.getUserByUsername(username);

        // Определите, является ли пользователь администратором
        boolean isAdmin = currentUser != null && currentUser.isAdmin();

        // Добавьте атрибут в модель
        model.addAttribute("isAdmin", isAdmin);

        return "index";
    }
}
