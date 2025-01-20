package com.example.productmaster.Repo;

import com.example.productmaster.DTO.UserDto;
import com.example.productmaster.Entity.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface UserRepo extends JpaRepository<MyUser, Long> {
    Optional<MyUser> findByEmail(String email);


}
