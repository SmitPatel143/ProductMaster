package com.example.productmaster.Repo;

import com.example.productmaster.Entity.Cart;
import com.example.productmaster.Entity.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepo extends JpaRepository<Cart, Long> {
    Cart findByUser(MyUser user);
}
