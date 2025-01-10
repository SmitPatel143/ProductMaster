package com.example.productmaster.Repo;

import com.example.productmaster.Entity.ConfirmationToken;
import com.example.productmaster.Entity.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationTokenRepo extends JpaRepository<ConfirmationToken, Long> {
    Optional<ConfirmationToken> findByToken(String token);

    void deleteByUser(MyUser user);
}
