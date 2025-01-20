package com.example.productmaster.Controller;

import com.example.productmaster.DTO.*;
import com.example.productmaster.Entity.MyUser;
import com.example.productmaster.Entity.Role;
import com.example.productmaster.Repo.RoleRepo;
import com.example.productmaster.Repo.UserRepo;
import com.example.productmaster.Service.AdminService;
import com.example.productmaster.Service.MyUserDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RestController
@RequestMapping("/admin/")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepo userRepo;
    private final MyUserDetailsService myUserDetailsService;
    private final AdminService adminService;
    private final RoleRepo roleRepo;

    @PostMapping("/assignRole")
    public ResponseEntity<?> addRoleToUser(@Valid @RequestBody RoleDto roleDto) {
        MyUser user = userRepo.findById(roleDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Failed to assign role. User not found"));
        Role role = roleRepo.findById(roleDto.getRoleId())
                .orElseThrow(() -> new IllegalArgumentException("Failed to assign role. Role not found"));
        if (user.getRoles().contains(role))
            throw new IllegalArgumentException("Role already assigned to the user");
        user.getRoles().add(role);
        userRepo.save(user);
        return ResponseEntity.status(HttpStatus.OK).body("Role assigned to the user");
    }

    @GetMapping("/allUser")
    public ResponseEntity<?> allUser() {
        log.info("Get all user");
        List<MyUser> users = myUserDetailsService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/products/getAll")
    public ResponseEntity<?> getAllProducts() {
        log.info("Get all products");
        return adminService.getAllProducts();
    }

    @GetMapping("/products/getByWsCode/{wsCode}")
    public ResponseEntity<?> getProductsByWsCode(@PathVariable String wsCode) {
        log.info("Get all products by wsCode: {}", wsCode);
        return adminService.getProductByWsCode(wsCode);
    }

    @PostMapping("/products/update")
    public ResponseEntity<?> updateProduct(@Valid @RequestBody ProductDto productDto) {
        return adminService.updateProduct(productDto);
    }

    @DeleteMapping("/products/deactivation/{wsCode}")
    public ResponseEntity<?> deactivationOfProduct(@PathVariable String wsCode) {
        return adminService.deactivateProduct(wsCode);
    }

    @PostMapping("/products/save")
    public ResponseEntity<?> saveProduct(@Valid @RequestBody ProductDto productDto) {
        log.info(productDto.toString());
        return adminService.saveProduct(productDto);
    }

    @PostMapping("/category/save")
    public ResponseEntity<?> saveCategory(@Valid @RequestBody CategoryDto categoryDto) {
        return adminService.saveCategory(categoryDto);
    }

    @GetMapping ("/category/getAll")
    public ResponseEntity<?> getAllCategory() {
        return adminService.getAllActiveCategories();
    }

//    @PostMapping("/category/update")
//    public ResponseEntity<?> updateCategory(@Valid @RequestBody CategoryDto categoryDto){
//
//    }
//
//    @PostMapping("/category/deactivation")
//    public ResponseEntity<?> deactivateCategory(@Valid @RequestBody CategoryDto categoryDto){
//
//    }
//
//    @PostMapping("category/get")
//    public ResponseEntity<?> getCategory(@Valid @RequestBody CategoryDto categoryDto){
//
//    }

}
