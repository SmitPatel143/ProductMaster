package com.example.productmaster.Repo;

import com.example.productmaster.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepo extends JpaRepository<Role, Long> {
    Role findByName(String name);

    Role getByName(String name);
}
