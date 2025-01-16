package com.example.productmaster.Controller;

import com.example.productmaster.DTO.RegisterUser;
import com.example.productmaster.Service.AuthenticationService;
import com.example.productmaster.Service.MyUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/register")
@RequiredArgsConstructor
@Slf4j
public class RegisterController {

    private final MyUserDetailsService userDetailsService;
    private final AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity<?> register(@Valid @RequestBody RegisterUser user, HttpServletRequest request) {
        log.info("Registering new user");
        return authenticationService.registerNewUserAccount(user, request);
    }

    @GetMapping("/confirm")
    public ResponseEntity<?> confirm(@RequestParam("token") String token) {
        log.info("Confirming token");
        return ResponseEntity.ok(authenticationService.confirmUser(token));
    }
}
