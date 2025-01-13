//package com.example.productmaster.UnitTest;
//
//import com.example.productmaster.DTO.ProductDto;
//import com.example.productmaster.Entity.Product;
//import com.example.productmaster.Exception.FailedToSaveProductException;
//import com.example.productmaster.Exception.NoProductFound;
//import com.example.productmaster.Repo.ProductRepo;
//import com.example.productmaster.Service.AdminService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class AdminServiceTest {
//
//    @Mock
//    private ProductRepo productRepo;
//
//    @InjectMocks
//    private AdminService adminService;
//
//    private Product product;
//    private ProductDto productDto;
//
//    @BeforeEach
//    void setUp() {
//        product = new Product();
//        product.setWsCode("WS001");
//        product.setName("Test Product");
//        product.setActiveStatus(true);
//
//        productDto = new ProductDto("Test1", "55-inch LED TV",
//                "http://example.com/img/samsung-tv.jpg", 499.99f, 599.99f,
//                50, 1.0f, List.of());
//    }
//
//    @Test
//    void testSaveProduct_ShouldSaveProduct() {
//        adminService.saveProduct(productDto);
//        verify(productRepo, times(1)).save(any(Product.class));
//    }
//
//    @Test
//    void testSaveProduct_ShouldThrowException_WhenSaveFails() {
//        doThrow(new RuntimeException("Database error")).when(productRepo).save(any(Product.class));
//        assertThrows(FailedToSaveProductException.class, () -> adminService.saveProduct(productDto));
//    }
//
//    @Test
//    void testDeactivateProduct_ShouldDeactivateProduct_WhenProductExists() {
//        when(productRepo.findByWsCode("WS001")).thenReturn(product);
//        String result = adminService.deactivateProduct("WS001");
//        verify(productRepo, times(1)).deactivateProduct("WS001");
//        assertEquals("Product Deactivated", result);
//    }
//
//    @Test
//    void testDeactivateProduct_ShouldThrowException_WhenProductNotFound() {
//        when(productRepo.findByWsCode("WS001")).thenReturn(null);
//        assertThrows(NoProductFound.class, () -> adminService.deactivateProduct("WS001"));
//    }
//
//    @Test
//    void testGetAllActiveProducts_ShouldReturnActiveProducts() {
//        when(productRepo.getAllActiveProduct()).thenReturn(List.of(product));
//        List<Product> result = adminService.getAllActiveProducts();
//        assertEquals(1, result.size());
//        assertTrue(result.getFirst().isActiveStatus());
//    }
//
//    @Test
//    void testGetAllActiveProducts_ShouldThrowException_WhenNoActiveProducts() {
//        when(productRepo.getAllActiveProduct()).thenThrow(new RuntimeException("No active products"));
//        assertThrows(FailedToSaveProductException.class, () -> adminService.getAllActiveProducts());
//    }
//
//    @Test
//    void testUpdateProduct_ShouldUpdateProduct_WhenProductExists() {
//        when(productRepo.findByWsCode("WS001")).thenReturn(product);
//        String result = adminService.updateProduct(productDto);
//        verify(productRepo, times(1)).saveAndFlush(any(Product.class));
//        assertEquals("Product Updated Successfully", result);
//    }
//
//    @Test
//    void testUpdateProduct_ShouldThrowException_WhenProductNotFound() {
//        when(productRepo.findByWsCode("WS001")).thenReturn(null);
//        assertThrows(FailedToSaveProductException.class, () -> adminService.updateProduct(productDto));
//    }
//}
