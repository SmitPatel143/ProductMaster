package com.example.productmaster.Controller;

import com.example.productmaster.DTO.LoginUserDto;
import com.example.productmaster.Service.AuthenticationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user/")
public class LoginController {

    private final AuthenticationService authenticationService;

    public LoginController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/login")
    String login() {
        return "login";
    }



}
