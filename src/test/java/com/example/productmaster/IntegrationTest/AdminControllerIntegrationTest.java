//package com.example.productmaster.IntegrationTest;
//
//import com.example.productmaster.Controller.AdminController;
//import com.example.productmaster.Entity.MyUser;
//import com.example.productmaster.Entity.Role;
//import com.example.productmaster.Repo.UserRepo;
//import com.example.productmaster.Service.AdminService;
//import com.example.productmaster.Service.JWTService;
//import com.example.productmaster.Service.MyUserDetailsService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(AdminController.class)
//public class AdminControllerIntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockitoBean
//    private UserRepo userRepo;
//
//    @MockitoBean
//    private MyUserDetailsService myUserDetailsService;
//
//    @MockitoBean
//    private AdminService adminService;
//
//    @BeforeEach
//    void setUp() {
//
//    }
//
//    @Test
//    @WithMockUser(roles = "ADMIN") // Simulate an authenticated user with 'ADMIN' role
//    void testAddRoleToUser() throws Exception {
//        MyUser user = new MyUser();
//        Role role = new Role();
//        user.setFirstName("testuser");
//        role.setName("ADMIN");
//
//        when(userRepo.save(any(MyUser.class))).thenReturn(user);
//
//        mockMvc.perform(post("/admin/assignRole")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(user))
//                )
//                .andExpect(status().isOk())
//                .andExpect(content().string("Role assigned to the user"));
//        verify(userRepo, times(1)).save(user);
//    }
//
//}
