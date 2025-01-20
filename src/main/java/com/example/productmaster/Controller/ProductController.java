package com.example.productmaster.Controller;


import com.example.productmaster.DTO.CartDto;
import com.example.productmaster.DTO.ProductDto;
import com.example.productmaster.Repo.CartItemsRepo;
import com.example.productmaster.Repo.CartRepo;
import com.example.productmaster.Repo.ProductRepo;
import com.example.productmaster.Service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/product")
@AllArgsConstructor
public class ProductController {
    private ProductService productService;
    private ProductRepo productRepo;
    private CartRepo cartRepo;
    private CartItemsRepo cartItemsRepo;

    @GetMapping("/getAll")
    private ResponseEntity<?> getAllActiveProducts() {
        return productService.getAllActiveProducts();
    }

    @PostMapping("/addToCart")
    private ResponseEntity<?> addProductsIntoCart(@RequestBody CartDto cart) {
        return productService.saveProductsIntoCart(cart);
    }

    @GetMapping("/fetchUserCart/{username}")
    private ResponseEntity<?> fetchUserCart(@PathVariable String username) {
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


}
