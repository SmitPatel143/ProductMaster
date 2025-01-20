package com.example.productmaster.DTO;

import com.example.productmaster.Entity.ProductImages;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
@Validated
public class ProductDto {

    private Long id;

    private String wsCode;

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    @NotEmpty
    private List<String> imageURL;

    private String imagePath;

    @NotNull
    private float salesPrice;

    @NotNull
    private int quantity;
    @NotNull
    private int mrp;
    @NotNull
    private float packageSize;

    private String categoryId;

    private String categoryName;

    private boolean activeStatus;

    public ProductDto(Long id,String wsCode, String name, String description, float salesPrice,int quantity,  int MRP, float packageSize,String categoryName, String categoryId, boolean activeStatus) {
        this.id = id;
        this.wsCode = wsCode;
        this.name = name;
        this.description = description;
        this.salesPrice = salesPrice;
        this.mrp = MRP;
        this.quantity = quantity;
        this.packageSize = packageSize;
        this.categoryId = categoryId;
        this.activeStatus = activeStatus;
    }

    public ProductDto(Long id, String wsCode, String name, String description, float salesPrice, int quantity, int mrp, float packageSize, String categoryName, String categoryId, boolean activeStatus, String imagePath) {
        this.id = id;
        this.wsCode = wsCode;
        this.name = name;
        this.description = description;
        this.salesPrice = salesPrice;
        this.quantity = quantity;
        this.mrp = mrp;
        this.packageSize = packageSize;
        this.categoryName = categoryName;
        this.activeStatus = activeStatus;
        this.categoryId = categoryId;
        this.imagePath = imagePath;
    }

    private List<String> convertImageURL(List<ProductImages> imagesList) {
        return imagesList.stream().map(ProductImages::getImageURL).collect(Collectors.toList());
    }
}
