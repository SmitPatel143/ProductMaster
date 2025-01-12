package com.example.productmaster.Controller;

import com.example.productmaster.DTO.RegisterUser;
import com.example.productmaster.Service.AuthenticationService;
import com.example.productmaster.Service.MyUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/register")
public class RegisterController {

    private final MyUserDetailsService userDetailsService;
    private final AuthenticationService authenticationService;

    private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);

    public RegisterController(MyUserDetailsService userDetailsService, AuthenticationService authenticationService) {
        this.userDetailsService = userDetailsService;
        this.authenticationService = authenticationService;
    }

    @PostMapping
    public ResponseEntity<?> register(@Valid @RequestBody RegisterUser user, HttpServletRequest request) {
        logger.info("Registering new user");
        return ResponseEntity.ok(authenticationService.registerNewUserAccount(user, request));
    }

    @GetMapping("/confirm")
    public ResponseEntity<?> confirm(@RequestParam("token") String token) {
        logger.info("Confirming token");
        return ResponseEntity.ok(authenticationService.confirmUser(token));
    }
}
