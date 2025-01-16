package com.example.productmaster.Repo;

import com.example.productmaster.Entity.Category;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {
    Optional<Category> findByCategoryId(String categoryId);

//    @Modifying
//    @Transactional
//    @Query("UPDATE Category c SET c = false WHERE p.wsCode = :wsCode")
//    void deactivateProduct(String wsCode);
}
