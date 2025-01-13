package com.example.productmaster.Controller;

import com.example.productmaster.DTO.LoginUserDto;
import com.example.productmaster.Service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user/")
@RequiredArgsConstructor
public class LoginController {

    private final AuthenticationService authenticationService;

    @GetMapping("/login")
    String login() {
        return "login";
    }


}
