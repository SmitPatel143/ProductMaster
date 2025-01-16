package com.example.productmaster.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class ApiResponse<T> {
    private int status;
    private String message;
    private T data;
}
