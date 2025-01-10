package com.example.productmaster.Service;

import com.example.productmaster.DTO.RegisterUser;
import com.example.productmaster.Entity.ConfirmationToken;
import com.example.productmaster.Entity.MyUser;
import com.example.productmaster.Exception.ConfirmationTokenExpiredException;
import com.example.productmaster.Exception.UserIsAlreadyRegisteredException;
import com.example.productmaster.Repo.ConfirmationTokenRepo;
import com.example.productmaster.Repo.UserRepo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final ConfirmationTokenRepo tokenRepo;

    public MyUserDetailsService(UserRepo userRepo, PasswordEncoder passwordEncoder, EmailService emailService, ConfirmationTokenRepo tokenRepository) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.tokenRepo = tokenRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MyUser user = userRepo.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found with this email"));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.getAuthorities());
    }

    public String registerUser(RegisterUser user, HttpServletRequest request) {

        Optional<MyUser> existingUser = userRepo.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            MyUser alreadyRegisteredUser = existingUser.get();
            if (alreadyRegisteredUser.isEnabled()) {
                throw new UserIsAlreadyRegisteredException("User already registered and confirmed.");
            }
            return resendConfirmationEmail(alreadyRegisteredUser, request);
        }

        MyUser currentUser = new MyUser(user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                passwordEncoder.encode(user.getPassword()));
        userRepo.save(currentUser);
        return sendingConfirmationEmail(currentUser, request);

    }

    public String confirmUser(String token) {
        ConfirmationToken tokenToConfirm = tokenRepo.findByToken(token)
                .orElseThrow(() -> new ConfirmationTokenExpiredException("Session Expired, Register again"));

        if (tokenToConfirm.getExpiresAt().isBefore(LocalDateTime.now()))
            throw new ConfirmationTokenExpiredException("Link has expired, Register again");

        MyUser user = tokenToConfirm.getUser();
        user.setEnabled(true);
        userRepo.save(user);
        tokenRepo.delete(tokenToConfirm);
        return "User has been confirmed successfully";
    }

    private String resendConfirmationEmail(MyUser user, HttpServletRequest request) {
        tokenRepo.deleteByUser(user);
        ConfirmationToken newToken = creatingConfirmationToken(user);
        tokenRepo.save(newToken);
        String confirmationLink = generateConfirmationLink(newToken.getToken(), request);
        String emailContent = "<html><body>" +
                "<p>Click the link to confirm your email:</p>" +
                "<a href=\"" + confirmationLink + "\">" + confirmationLink + "</a>" +
                "</body></html>";

        emailService.sendConfirmationMail(
                user.getEmail(),
                "Confirm Your Email",
                emailContent);
        return "User has been registered successfully, Please check your email to confirm your account";
    }

    private String sendingConfirmationEmail(MyUser user, HttpServletRequest request) {
        ConfirmationToken confirmationToken =creatingConfirmationToken(user);
        tokenRepo.save(confirmationToken);

        String confirmationLink = generateConfirmationLink(confirmationToken.getToken(), request);
        String emailContent = "<html><body>" +
                "<p>Click the link to confirm your email:</p>" +
                "<a href=\"" + confirmationLink + "\">" + confirmationLink + "</a>" +
                "</body></html>";

        emailService.sendConfirmationMail(
                user.getEmail(),
                "Confirm Your Email",
                emailContent);
        return "User has been registered successfully, Please check your email to confirm your account";
    }

    private String generateConfirmationLink(String token, HttpServletRequest request) {
        return request.getRequestURI() + "confirm?token=" + token;
    }

    private ConfirmationToken creatingConfirmationToken(MyUser user){
        return new ConfirmationToken(user);
    }


}
