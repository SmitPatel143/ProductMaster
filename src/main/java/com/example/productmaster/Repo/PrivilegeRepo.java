package com.example.productmaster.Repo;

import com.example.productmaster.Entity.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivilegeRepo extends JpaRepository<Privilege, Long> {
    Privilege findByName(String name);
}
