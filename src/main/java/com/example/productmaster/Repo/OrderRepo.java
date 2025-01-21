package com.example.productmaster.Repo;

import com.example.productmaster.Entity.MyUser;
import com.example.productmaster.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o " +
            "LEFT JOIN FETCH o.orderItems items " +
            "LEFT JOIN FETCH items.product " +
            "LEFT JOIN FETCH o.user " +
            "WHERE o.user.id = :userId")
    List<Order> findByUser(@Param("userId") Long userId);
}
