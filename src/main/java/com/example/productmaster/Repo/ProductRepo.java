package com.example.productmaster.Repo;

import com.example.productmaster.DTO.ProductDto;
import com.example.productmaster.Entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
@Transactional
public interface ProductRepo extends JpaRepository<Product, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.activeStatus = false WHERE p.wsCode = :wsCode")
    void deactivateProduct(String wsCode);

    @Query("SELECT new com.example.productmaster.DTO.ProductDto(p.id,p.wsCode, p.name, p.description,p.salesPrice,p.quantity,p.mrp, p.packageSize,p.category.name,p.category.categoryId,p.activeStatus) FROM Product p")
    List<ProductDto> getAll();

    @Query("SELECT new com.example.productmaster.DTO.ProductDto(p.id, p.wsCode, p.name, p.description, p.salesPrice, p.quantity, p.mrp, p.packageSize, c.name, c.categoryId, p.activeStatus, pi.imageURL) " +
            "FROM Product p " +
            "JOIN p.category c " +
            "LEFT JOIN p.productImagesList pi " +
            "WHERE p.activeStatus = true")
    List<ProductDto> getAllActiveProducts();

    Optional<Product> findByWsCode(String wsCode);

    Product getProductsByWsCode(String wsCode);
}
