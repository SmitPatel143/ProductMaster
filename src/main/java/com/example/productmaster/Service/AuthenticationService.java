package com.example.productmaster.Service;

import com.example.productmaster.DTO.RegisterUser;
import com.example.productmaster.Entity.ConfirmationToken;
import com.example.productmaster.Entity.MyUser;
import com.example.productmaster.Entity.Role;
import com.example.productmaster.Exception.*;
import com.example.productmaster.Repo.ConfirmationTokenRepo;
import com.example.productmaster.Repo.RoleRepo;
import com.example.productmaster.Repo.UserRepo;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
@Transactional
public class AuthenticationService {
    private final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    private final UserRepo userRepo;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepo roleRepo;
    private final EmailService emailService;
    private final ConfirmationTokenRepo tokenRepo;

    public AuthenticationService(UserRepo userRepo, BCryptPasswordEncoder passwordEncoder, EmailService emailService, ConfirmationTokenRepo tokenRepository, RoleRepo roleRepo) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.tokenRepo = tokenRepository;
        this.roleRepo = roleRepo;
    }

    public String registerNewUserAccount(RegisterUser user, HttpServletRequest request) {

        Optional<MyUser> existingUser = userRepo.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            MyUser alreadyRegisteredUser = existingUser.get();
            if (alreadyRegisteredUser.isEnabled()) {
                throw new UserIsAlreadyRegisteredException("User already registered and confirmed.");
            }
            return resendConfirmationEmail(alreadyRegisteredUser, request);
        }

        Role roleUser = roleRepo.findByName("ROLE_USER");
        Set<Role> roles = Set.of(roleUser);
        logger.info(String.valueOf(roles));
        MyUser currentUser = new MyUser(user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                passwordEncoder.encode(user.getPassword()), roles);
        userRepo.save(currentUser);
        return sendingConfirmationEmail(currentUser, request);

    }

    public String confirmUser(String token) {
        ConfirmationToken tokenToConfirm = tokenRepo.findByToken(token)
                .orElseThrow(() -> new ConfirmationTokenExpiredException("Invalid Link"));

        if (tokenToConfirm.getExpiresAt().isBefore(LocalDateTime.now()))
            throw new ConfirmationTokenExpiredException("Link has expired, Register again");

        MyUser user = tokenToConfirm.getUser();
        user.setEnabled(true);
        userRepo.save(user);
        tokenRepo.delete(tokenToConfirm);
        return "User has been confirmed successfully";
    }

    private String resendConfirmationEmail(MyUser user, HttpServletRequest request) {
        ConfirmationToken existingToken = tokenRepo.getByUserId(user.getId());
        if (existingToken != null && existingToken.getExpiresAt().isAfter(LocalDateTime.now())) {
            return "A confirmation email has already been sent to your email address. Please check your inbox.";
        }

        if (existingToken != null) {
            tokenRepo.deleteByUser(user);
        }
        ConfirmationToken newToken = creatingConfirmationToken(user);
        tokenRepo.save(newToken);
        sendingMail(newToken, request, user.getEmail());
        return "User has been registered successfully, Please check your email to confirm your account";
    }

    private String sendingConfirmationEmail(MyUser user, HttpServletRequest request) {
        ConfirmationToken confirmationToken = creatingConfirmationToken(user);
        tokenRepo.save(confirmationToken);
        sendingMail(confirmationToken, request, user.getEmail());
        return "User has been registered successfully, Please check your email to confirm your account";
    }

    private void sendingMail(ConfirmationToken token, HttpServletRequest request, String to) {
        String confirmationLink = generateConfirmationLink(token.getToken(), request);
        String emailContent = "<html><body>" +
                "<p>Click the link to confirm your email:</p>" +
                "<a href=\"" + confirmationLink + "\">" + confirmationLink + "</a>" +
                "</body></html>";
        emailService.sendConfirmationMail(
                to,
                "Confirm Your Email",
                emailContent);
    }

    private String generateConfirmationLink(String token, HttpServletRequest request) {
        return ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath("/user/register/confirm")
                .queryParam("token", token)
                .toUriString();
    }

    private ConfirmationToken creatingConfirmationToken(MyUser user) {
        return new ConfirmationToken(user);
    }
}
