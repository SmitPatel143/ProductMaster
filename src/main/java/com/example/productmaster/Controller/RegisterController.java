package com.example.productmaster.Controller;

import com.example.productmaster.DTO.RegisterUser;
import com.example.productmaster.Service.MyUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/register")
public class RegisterController {

    @Autowired
    private MyUserDetailsService userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);

    @PostMapping
    public ResponseEntity<String> register(@Valid @RequestBody RegisterUser user, HttpServletRequest request) {
        logger.info("Registering new user");
        return ResponseEntity.ok(userDetailsService.registerUser(user, request));
    }
}
