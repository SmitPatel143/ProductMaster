package com.example.productmaster.Service;

import com.example.productmaster.DTO.ApiResponse;
import com.example.productmaster.DTO.CategoryDto;
import com.example.productmaster.DTO.ProductDto;
import com.example.productmaster.Entity.Category;
import com.example.productmaster.Entity.Product;
import com.example.productmaster.Entity.ProductImages;
import com.example.productmaster.Exception.CategoryNotFoundException;
import com.example.productmaster.Exception.FailedToSaveProductException;
import com.example.productmaster.Exception.ResourceNotFoundException;
import com.example.productmaster.Repo.CategoryRepo;
import com.example.productmaster.Repo.ProductRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.procedure.ProcedureOutputs;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class AdminService {
    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;

    public ResponseEntity<ApiResponse<String>> saveProduct(ProductDto productDto) {

        Category category = categoryRepo.findByCategoryId(productDto.getCategoryId());

        if(category == null)
            return new ResponseEntity<>(setApiResponse(HttpStatus.NOT_FOUND.value(), "Category not found with this categoryId, Please try again later", null), HttpStatus.NOT_FOUND);

        List<ProductImages> productImagesList = productDto.getImageURL().stream()
                .map(ProductImages::new)
                .toList();

        Product product = new Product(productDto.getName(), productDto.getDescription(), productImagesList, productDto.getSalesPrice(), productDto.getMrp(), productDto.getQuantity(), productDto.getPackageSize(), category);

        try {
            productRepo.save(product);
            return new ResponseEntity<>(setApiResponse(HttpStatus.CREATED.value(), "Product Saved Successfully !", null), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(setApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to save product, Please try again later", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ApiResponse<List<ProductDto>>> getAllProducts() {
        try {
            List<ProductDto> productsList=  productRepo.getAll();
            if(productsList.isEmpty())
                return new ResponseEntity<>(setApiResponse(HttpStatus.NOT_FOUND.value(), "There are no active products", null), HttpStatus.NOT_FOUND);

            log.info("Products {}", productsList);
            return new ResponseEntity<>(setApiResponse(HttpStatus.OK.value(), "Products available", productsList), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(setApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to get products, Please try again later", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ApiResponse<Product>> deactivateProduct(String wsCode) {
        Optional<Product> product = productRepo.findByWsCode(wsCode);
        if(product.isEmpty() || !product.get().isActiveStatus())
            return new ResponseEntity<>(setApiResponse(HttpStatus.NOT_FOUND.value(), "Product not found with this wsCode, Please try again later", null), HttpStatus.NOT_FOUND);

        try {
            productRepo.deactivateProduct(wsCode);
            return new ResponseEntity<>(setApiResponse(HttpStatus.OK.value(), "Product Deactivated Successfully !", null), HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(setApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to deactivate !",null),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ApiResponse<Product>> getProductByWsCode(String wsCode) {
        Optional<Product> product = productRepo.findByWsCode(wsCode);
        if(product.isEmpty())
            return new ResponseEntity<>(setApiResponse(HttpStatus.NOT_FOUND.value(), "Product not found with this wsCode", null), HttpStatus.NOT_FOUND);
        try{
            return new ResponseEntity<>(setApiResponse(HttpStatus.OK.value(), "Product Found Successfully !", product.get()), HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(setApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Failed to retrieve product !",null),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> updateProduct(ProductDto productDto) {
        Optional<Product> optionalProduct = productRepo.findByWsCode(productDto.getWsCode());

        if (optionalProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(setApiResponse(HttpStatus.NOT_FOUND.value(), "Product not found with this wsCode, Please try again later", null));
        }

        try {
            Product existingProduct = optionalProduct.get();
            Product updatedProduct = new Product(productDto, existingProduct,categoryRepo.findByCategoryId(productDto.getCategoryId()));

            productRepo.save(updatedProduct);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(setApiResponse(HttpStatus.OK.value(), "Product Updated Successfully!", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(setApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to update the product, Please try again later", null));
        }
    }


    public ResponseEntity<ApiResponse<String>> saveCategory(CategoryDto categoryDto) {
        try {
            categoryRepo.save(new Category(categoryDto));
            return new ResponseEntity<>(setApiResponse(201, "Category Saved Successfully !", null), HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            log.error("Category already exists {}", categoryDto.getName());
            return new ResponseEntity<>(setApiResponse(409, "Category already exists", null), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error("Failed to save category", e);
            return new ResponseEntity<>(setApiResponse(500, "Failed to save category", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ApiResponse<List<CategoryDto>>> getAllActiveCategories() {
        List<Category> categoryList = categoryRepo.findAllByActive(true);
        if (categoryList.isEmpty())
            return new ResponseEntity<>(setApiResponse(HttpStatus.NOT_FOUND.value(), "There are no active category", null), HttpStatus.NOT_FOUND);
        log.info("Active Categories {}", categoryList);

        List<CategoryDto> categoryDtoList = categoryList.stream()
                .map(category -> new CategoryDto(category.getCategoryId(), category.getName(), category.getDescription()))
                .toList();
        return new ResponseEntity<>(setApiResponse(HttpStatus.OK.value(), "Category available", categoryDtoList), HttpStatus.OK);


    }

    public ResponseEntity<ApiResponse<String>> updateCategory(CategoryDto categoryDto) {
        Category category = categoryRepo.findByCategoryId(categoryDto.getCategoryId());
        if(category == null)
            return new ResponseEntity<>(setApiResponse(HttpStatus.NOT_FOUND.value(), "Category not found with this categoryId, Please try again later", null), HttpStatus.NOT_FOUND);
        try {
            categoryRepo.saveAndFlush(category);
            return new ResponseEntity<>(setApiResponse(HttpStatus.OK.value(), "Category Updated Successfully !", null), HttpStatus.OK);
        } catch (Exception e) {
            throw new FailedToSaveProductException("Failed to update the category, Please try again later");
        }
    }

    private <T> ApiResponse<T> setApiResponse(final int value, final String message, final T data) {
        return new ApiResponse<>(value, message, data);
    }

}
