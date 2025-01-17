package com.example.productmaster.Service;

import com.example.productmaster.DTO.ApiResponse;
import com.example.productmaster.DTO.CartDto;
import com.example.productmaster.DTO.ProductDto;
import com.example.productmaster.Entity.Cart;
import com.example.productmaster.Entity.CartItems;
import com.example.productmaster.Entity.MyUser;
import com.example.productmaster.Repo.CartItemsRepo;
import com.example.productmaster.Repo.CartRepo;
import com.example.productmaster.Repo.CategoryRepo;
import com.example.productmaster.Repo.ProductRepo;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class ProductService {

    private ProductRepo productRepo;
    private CategoryRepo categoryRepo;
    private CartRepo cartRepo;
    private CartItemsRepo cartItemsRepo;


    public ResponseEntity<ApiResponse<List<ProductDto>>> getAllActiveProducts() {
        List<ProductDto> activeProductList = productRepo.getAllActiveProducts();

        if (activeProductList.isEmpty())
            return new ResponseEntity<>(setApiResponse(HttpStatus.NOT_FOUND.value(), "Currently there are no active products", null), HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(setApiResponse(HttpStatus.OK.value(), "Product Available !", activeProductList), HttpStatus.OK);
    }

    public ResponseEntity<?> saveProductsIntoCart(CartDto cart) {
        MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user == null)
            new ResponseEntity<>(setApiResponse(HttpStatus.UNAUTHORIZED.value(), "You are not logged in", null), HttpStatus.UNAUTHORIZED);

        try {
            Cart userCart = cartRepo.findByUser(user);
            if (userCart == null)
                userCart = new Cart(user);
            CartItems cartItems = new CartItems(productRepo.getProductsByWsCode(cart.getProductId()), cart.getQuantity(), cart.getTotalPrice(), userCart);
            cartItemsRepo.save(cartItems);
            cartRepo.save(userCart);
            return new ResponseEntity<>(setApiResponse(HttpStatus.OK.value(), "Product added", null), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(setApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private <T> ApiResponse<T> setApiResponse(final int value, final String message, final T data) {
        return new ApiResponse<>(value, message, data);
    }
}
