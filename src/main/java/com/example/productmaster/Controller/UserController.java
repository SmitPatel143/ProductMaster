package com.example.productmaster.Controller;

import com.example.productmaster.Entity.MyUser;
import com.example.productmaster.Service.MyUserDetailsService;
import com.example.productmaster.Service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final MyUserDetailsService myUserDetailsService;
    private final ProductService productService;

    @GetMapping("/products/getAllActive")
    private ResponseEntity<?> getAllActiveProducts() {
        return productService.getAllActiveProducts();
    }


}
