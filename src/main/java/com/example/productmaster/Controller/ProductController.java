package com.example.productmaster.Controller;
import com.example.productmaster.DTO.CartDto;
import com.example.productmaster.DTO.OrderDto;
import com.example.productmaster.Entity.MyUser;
import com.example.productmaster.Repo.UserRepo;
import com.example.productmaster.Service.OrderService;
import com.example.productmaster.Service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/product")
@AllArgsConstructor
public class ProductController {
    private final UserRepo userRepo;
    private ProductService productService;
    private OrderService orderService;

    @GetMapping("/getAll")
    private ResponseEntity<?> getAllActiveProducts() {
        return productService.getAllActiveProducts();
    }

    @PostMapping("/addToCart")
    private ResponseEntity<?> addProductsIntoCart(@RequestBody CartDto cart) {
        return productService.saveProductsIntoCart(cart);
    }

    @GetMapping("/fetchUserCart")
    private ResponseEntity<?> fetchUserCart(@RequestParam String username) {
        return productService.fetchUserCart(username);
    }

    @PostMapping("/updateQuantity")
    private ResponseEntity<?> updateProductQuantityInCart(@RequestBody CartDto cartDto) {
        return productService.updateProductQuantityInUserCart(cartDto);
    }

    @DeleteMapping("/removeFromCart/{cartId}")
    private ResponseEntity<?> removeProductFromCart(@PathVariable Long cartId) {
        return productService.removeFromCart(cartId);
    }

    @PostMapping("/placeOrder")
    private ResponseEntity<?> placeOrder(@RequestBody OrderDto order) {
        return orderService.placeOrder(order.getCartId(), order.getTotalPrice());
    }

    @GetMapping("/fetchUserOrder/{userId}")
    private ResponseEntity<?> fetchUserOrder(@PathVariable Long userId) {
        return orderService.fetchOrderByUserId(userId);

    }




}
