package com.example.productmaster.Service;

import com.example.productmaster.Entity.MyUser;
import com.example.productmaster.Exception.UserNotFoundException;
import com.example.productmaster.Repo.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;

    public MyUserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found with this email"));
    }

    public Optional<MyUser> getAllUsers() {
        try {
            Optional<MyUser> users = userRepo.findByEmail("smit.p@medkart.in");
            if (users.isEmpty())
                throw new UserNotFoundException("User not found");
            return users;
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException("User not found");
        }
    }


}
