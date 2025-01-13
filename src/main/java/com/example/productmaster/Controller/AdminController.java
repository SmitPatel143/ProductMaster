package com.example.productmaster.Controller;

import com.example.productmaster.DTO.CategoryDto;
import com.example.productmaster.DTO.ProductDto;
import com.example.productmaster.DTO.RoleDto;
import com.example.productmaster.DTO.UserDto;
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
import java.util.Optional;

@Slf4j
@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/admin/")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepo userRepo;
    private final MyUserDetailsService myUserDetailsService;
    private final AdminService adminService;
    private final RoleRepo roleRepo;

    @PostMapping("/assignRole")
    public ResponseEntity<?> addRoleToUser(@Valid RoleDto roleDto) {
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

    @GetMapping("/products/get")
    public ResponseEntity<?> getAllProducts() {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.getAllActiveProducts());
    }

    @PostMapping("/products/update")
    public ResponseEntity<?> updateProduct(@RequestBody ProductDto productDto) {
        adminService.updateProduct(productDto);
        return ResponseEntity.status(HttpStatus.OK).body("Product updated");
    }

    @PostMapping("/products/deactivation")
    public ResponseEntity<?> deactivationOfProduct(@RequestBody String wsCode) {
        adminService.deactivateProduct(wsCode);
        return ResponseEntity.status(HttpStatus.OK).body("Product deactivated");
    }

    @PostMapping("/products/save")
    public ResponseEntity<?> saveProduct(@RequestBody ProductDto productDto) {
        log.info(productDto.toString());
        ProductDto p1 = new ProductDto();
        p1.setMRP(1000);
        log.info("{} {}", p1.getMRP(), productDto.getMRP());
        System.out.println("value of the mrp is: "+productDto.getMRP());
        adminService.saveProduct(productDto);
        return ResponseEntity.status(HttpStatus.OK).body("Product saved");
    }

    @PostMapping("/products/category/save")
    public ResponseEntity<?> saveCategory(@RequestBody CategoryDto categoryDto){
        log.info(categoryDto.toString());
        adminService.saveCategory(categoryDto);
        return ResponseEntity.status(HttpStatus.OK).body("Category saved");
    }

}
