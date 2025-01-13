package com.example.productmaster.Service;

import com.example.productmaster.DTO.CategoryDto;
import com.example.productmaster.DTO.ProductDto;
import com.example.productmaster.Entity.Category;
import com.example.productmaster.Entity.Product;
import com.example.productmaster.Entity.ProductImages;
import com.example.productmaster.Exception.FailedToSaveProductException;
import com.example.productmaster.Exception.ResourceNotFoundException;
import com.example.productmaster.Repo.CategoryRepo;
import com.example.productmaster.Repo.ProductRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class AdminService {
    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;

    public void saveProduct(ProductDto productDto) {

        Category category = categoryRepo.findByCategoryId(productDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Failed to save product, Category not found, Please try again later"));
        List<ProductImages> productImagesList = productDto.getImageURL().stream()
                .map(ProductImages::new)
                .toList();

        Product product = new Product(productDto.getName(), productDto.getDescription(), productImagesList, productDto.getSalesPrice(),productDto.getMRP(), productDto.getQuantity(), productDto.getPackageSize(), category);

        try {
            productRepo.save(product);
        } catch (FailedToSaveProductException e) {
            log.error("Failed to save product", e);
            throw new FailedToSaveProductException("Failed to save product");
        }
    }

    public void deactivateProduct(String wsCode) {
        productRepo.findByWsCode(wsCode).orElseThrow(() -> new ResourceNotFoundException("Product not found with this wsCode, Please try again later"));
        productRepo.deactivateProduct(wsCode);
    }

    public List<Product> getAllActiveProducts() {
        try {
            return productRepo.getProductsByActiveStatus(true);
        } catch (Exception e) {
            log.error("Currently, There are not active products", e);
            throw new FailedToSaveProductException("Currently, There are not active products");
        }
    }

    public void updateProduct(ProductDto productDto) {
        Product product = productRepo.findByWsCode(productDto.getWsCode()).orElseThrow(() -> new ResourceNotFoundException("Product not found with this wsCode, Please try again later"));
        try {
            productRepo.saveAndFlush(product);
        } catch (Exception e) {
            throw new FailedToSaveProductException("Failed to update the product, Please try again later");
        }

    }

    public void saveCategory(CategoryDto categoryDto) {
        try {
            categoryRepo.save(new Category(categoryDto));
        } catch (Exception e) {
            log.error("Failed to save category", e);
            throw new FailedToSaveProductException("Failed to save category");
        }
    }

}
