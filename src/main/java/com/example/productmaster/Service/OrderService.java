package com.example.productmaster.Service;

import com.example.productmaster.DTO.ApiResponse;
import com.example.productmaster.Entity.Order;
import com.example.productmaster.Entity.OrderItems;
import com.example.productmaster.Entity.OrderStatus;
import com.example.productmaster.Entity.PaymentStatus;
import com.example.productmaster.Repo.CartItemsRepo;
import com.example.productmaster.Repo.CartRepo;
import com.example.productmaster.Repo.OrderItemsRepo;
import com.example.productmaster.Repo.OrderRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

    private final CartRepo cartRepo;
    private final OrderRepo orderRepo;

    public ResponseEntity<?> placeOrder(Long cartId, BigDecimal totalAmount) {
        return cartRepo.findById(cartId)
                .map(cart -> {
                    try {
                        List<OrderItems> orderItems = cart.getCartItems().stream()
                                .map(cartItem -> new OrderItems(
                                        cartItem.getProduct(),
                                        cartItem.getQuantity(),
                                        BigDecimal.valueOf(cartItem.getTotalPrice())
                                ))
                                .collect(Collectors.toList());

                        Order order = new Order(
                                cart.getUser(),
                                orderItems,
                                totalAmount,
                                PaymentStatus.PAID,
                                OrderStatus.PENDING
                        );

                        Order savedOrder = orderRepo.save(order);
                        cartRepo.delete(cart);
                        return ResponseEntity.status(HttpStatus.CREATED)
                                .body(new ApiResponse<>(
                                        HttpStatus.CREATED.value(),
                                        "Order created successfully",
                                        savedOrder
                                ));
                    } catch (Exception e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(new ApiResponse<>(
                                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                        "Failed to create order: " + e.getMessage(),
                                        null
                                ));
                    }
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(setApiResponse(
                                HttpStatus.NOT_FOUND.value(),
                                "Cart not found",
                                null
                        )));
    }

    private <T> ApiResponse<T> setApiResponse(final int value, final String message, final T data) {
        return new ApiResponse<>(value, message, data);
    }
}
