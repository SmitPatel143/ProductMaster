package com.example.productmaster.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class CartDto {
    private String productId;
    private int quantity;
    private double totalPrice;
    private String username;
    private Long cartId;
}
