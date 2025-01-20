package com.example.productmaster.Repo;

import com.example.productmaster.Entity.Cart;
import com.example.productmaster.Entity.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemsRepo extends JpaRepository<CartItems, Long> {

    List<CartItems> findByProductWsCodeAndCart(String productWsCode, Cart cart);

    @Query("SELECT ci FROM CartItems ci " +
            "JOIN ci.cart c " +
            "JOIN c.user u " +
            "JOIN ci.product p " +
            "WHERE u.email = :username")
    List<CartItems> findCartItemsByUsername(@Param("username") String username);


}
