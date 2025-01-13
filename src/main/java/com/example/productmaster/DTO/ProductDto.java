package com.example.productmaster.DTO;

import com.example.productmaster.Entity.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import java.util.List;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class ProductDto {

    private String wsCode;

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    @NotEmpty
    private List<String> imageURL;

    @NotNull
    private float salesPrice;



    @NotNull
    private int quantity;
    @NotNull
    private int MRP;
    @NotNull
    private float packageSize;

    private String categoryId;

    public ProductDto(String name, String description, List<String> imageURL, float salesPrice, int MRP, int quantity, float packageSize, String categoryId) {
        this.name = name;
        this.description = description;
        this.imageURL = imageURL;
        this.salesPrice = salesPrice;
        this.MRP = MRP;
        this.quantity = quantity;
        this.packageSize = packageSize;
        this.categoryId = categoryId;
    }
}
