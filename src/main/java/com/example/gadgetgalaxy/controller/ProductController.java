package com.example.gadgetgalaxy.controller;

import com.example.gadgetgalaxy.entity.Product;
import com.example.gadgetgalaxy.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public String viewProduct(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        return "product/view";
    }

    @PostMapping
    public String createProduct(@ModelAttribute Product product) {
        productService.saveProduct(product);
        return "redirect:/product";
    }

    @PutMapping("/{id}")
    public String updateProduct(@PathVariable Long id, @ModelAttribute Product product) {
        productService.saveProduct(product);
        return "redirect:/product/" + id;
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
        return "redirect:/";
    }


    @GetMapping
    public String listProducts(Model model, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 6); // 6 products per page
        Page<Product> productPage = productService.getProducts(pageable);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        model.addAttribute("isAdmin", isAdmin);

        return "product";
    }



    // Admin routes
    @GetMapping("/admin")
    public String adminListProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "admin/product/list";
    }

    @GetMapping("/admin/edit/{id}")
    public String adminEditProductForm(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        return "admin/product/edit";
    }

    @PostMapping("/admin/edit/{id}")
    public String adminEditProduct(@PathVariable Long id, @ModelAttribute Product product) {
        productService.saveProduct(product);
        return "redirect:/product/admin";
    }

    @GetMapping("/admin/create")
    public String adminCreateProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "admin/product/create";
    }

    @PostMapping("/admin/create")
    public String adminCreateProduct(@ModelAttribute Product product) {
        productService.saveProduct(product);
        return "redirect:/product/admin";
    }
}
