package com.example.gadgetgalaxy.controller;

import com.example.gadgetgalaxy.entity.CartItem;
import com.example.gadgetgalaxy.entity.Order;
import com.example.gadgetgalaxy.service.CartItemService;
import com.example.gadgetgalaxy.service.OrderService;
import com.example.gadgetgalaxy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.gadgetgalaxy.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartItemController {

    private static final Logger logger = LoggerFactory.getLogger(CartItemController.class);

    private final CartItemService cartItemService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @GetMapping
    public String listCartItems(Model model) {
        List<CartItem> cartItems = cartItemService.getAllCartItems();
        BigDecimal totalCartValue = cartItemService.calculateTotalCartValue();
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalCartValue", totalCartValue);
        return "cart-item/list";
    }

    @GetMapping("/{id}")
    public String viewCartItem(@PathVariable Long id, Model model) {
        model.addAttribute("cartItem", cartItemService.getCartItemById(id));
        return "cart-item/view";
    }

    @PostMapping
    public String createCartItem(@ModelAttribute CartItem cartItem, Model model) {
        cartItemService.saveCartItem(cartItem);
        return "redirect:/cart";
    }

    @PutMapping("/{id}")
    public String updateCartItem(@PathVariable Long id, @ModelAttribute CartItem cartItem, Model model) {
        cartItemService.saveCartItem(cartItem);
        return "redirect:/cart/" + id;
    }

    @DeleteMapping("/{id}")
    public String deleteCartItem(@PathVariable Long id, Model model) {
        cartItemService.removeItem(id);
        return "redirect:/cart";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId, RedirectAttributes redirectAttributes, Model model) {
        System.out.println("Trying to add product with ID: " + productId + " to cart.");
        try {
            cartItemService.addProductToCart(productId);
            redirectAttributes.addFlashAttribute("message", "Product added to cart successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error adding product to cart: " + e.getMessage());
        }
        return "redirect:/";
    }

    @PostMapping("/remove/{id}")
    public String removeItemFromCart(@PathVariable Long id, Model model) {
        cartItemService.removeItem(id);
        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clearCart(Model model) {
        cartItemService.clearCart();
        return "redirect:/cart";
    }

    @PostMapping("/checkout")
    public String checkout(Model model) {
        logger.info("Checkout method started");

        User user = userService.getCurrentLoggedUser();
        logger.info("Current user: {}", user);

        if (user == null) {
            logger.warn("User is not authenticated");
            return "redirect:/login";
        }

        orderService.createOrderFromCartItems(user);
        logger.info("Order created");

        cartItemService.clearCart();
        logger.info("Cart cleared");

        return "redirect:/cart";

    }


}
