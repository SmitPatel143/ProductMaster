package com.example.productmaster.Service;

import com.example.productmaster.DTO.ApiResponse;
import com.example.productmaster.DTO.CartDto;
import com.example.productmaster.DTO.ProductDto;
import com.example.productmaster.Entity.Cart;
import com.example.productmaster.Entity.CartItems;
import com.example.productmaster.Entity.MyUser;
import com.example.productmaster.Entity.Order;
import com.example.productmaster.Repo.*;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;
    private final CartRepo cartRepo;
    private final CartItemsRepo cartItemsRepo;
    private final UserRepo userRepo;
    private final OrderRepo orderRepo;


    public ResponseEntity<ApiResponse<?>> getAllActiveProducts() {
        List<ProductDto> activeProductList = productRepo.getAllActiveProducts();
        log.info(activeProductList.toString());
        if (activeProductList.isEmpty())
            return new ResponseEntity<>(setApiResponse(HttpStatus.NOT_FOUND.value(), "Currently there are no active products", null), HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(setApiResponse(HttpStatus.OK.value(), "Product Available !", activeProductList), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> saveProductsIntoCart(CartDto cart) {
        MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info(cart.toString());
        if (user == null)
            return new ResponseEntity<>(setApiResponse(HttpStatus.UNAUTHORIZED.value(), "You are not logged in", null), HttpStatus.UNAUTHORIZED);


        try {
            Cart userCart = cartRepo.findByUser(user);
            if (userCart == null) {
                userCart = new Cart(user);
                cartRepo.save(userCart);
            }
            List<CartItems> cartItems1 = cartItemsRepo.findByProductWsCodeAndCart(cart.getProductId(), userCart);
            if (!cartItemsRepo.findByProductWsCodeAndCart(cart.getProductId(), userCart).isEmpty())
                return new ResponseEntity<>(setApiResponse(HttpStatus.CONFLICT.value(), "Product already added", null), HttpStatus.CONFLICT);

            CartItems cartItems = new CartItems(productRepo.getProductsByWsCode(cart.getProductId()), cart.getQuantity(), cart.getTotalPrice(), userCart);
            cartItemsRepo.save(cartItems);

            return new ResponseEntity<>(setApiResponse(HttpStatus.OK.value(), "Product added", null), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(setApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity<?> fetchUserCart(String username) {
        List<CartItems> cartItems = cartItemsRepo.findCartItemsByUsername(username);
        if (cartItems.isEmpty())
            return new ResponseEntity<>(setApiResponse(HttpStatus.NOT_FOUND.value(), "", null), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(setApiResponse(HttpStatus.OK.value(), "", cartItems), HttpStatus.OK);
    }

    public ResponseEntity<?> updateProductQuantityInUserCart(CartDto cart) {
        try {
            Optional<CartItems> cartItems = cartItemsRepo.findById(cart.getCartId());

            if (cartItems.isEmpty())
                return new ResponseEntity<>(setApiResponse(HttpStatus.NOT_FOUND.value(), "", null), HttpStatus.NOT_FOUND);

            CartItems cartItem = cartItems.get();
            cartItem.setQuantity(cart.getQuantity());
            cartItem.setTotalPrice(cart.getTotalPrice());
            cartItemsRepo.save(cartItem);
            return new ResponseEntity<>(setApiResponse(HttpStatus.OK.value(), "", null), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(setApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> removeFromCart(Long CartId) {
        try {
            cartItemsRepo.deleteById(CartId);
            return new ResponseEntity<>(setApiResponse(HttpStatus.OK.value(), "", null), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(setApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




    private <T> ApiResponse<T> setApiResponse(final int value, final String message, final T data) {
        return new ApiResponse<>(value, message, data);
    }
}
