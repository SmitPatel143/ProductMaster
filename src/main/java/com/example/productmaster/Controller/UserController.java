package com.example.productmaster.Controller;

import com.example.productmaster.Entity.MyUser;
import com.example.productmaster.Service.MyUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
public class UserController {

    private final MyUserDetailsService myUserDetailsService;

    public UserController(MyUserDetailsService myUserDetailsService) {
        this.myUserDetailsService = myUserDetailsService;
    }

    @GetMapping("/allUser")
    public ResponseEntity<?> allUser() {
        log.info("Get all user");
        Optional<MyUser> users = myUserDetailsService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}
