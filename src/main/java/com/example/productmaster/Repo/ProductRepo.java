package com.example.productmaster.Repo;

import com.example.productmaster.Entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.activeStatus = false WHERE p.wsCode = :wsCode")
    void deactivateProduct(String wsCode);

    List<Product> getProductsByActiveStatus(boolean activeStatus);

    Optional<Product> findByWsCode(String wsCode);
}
