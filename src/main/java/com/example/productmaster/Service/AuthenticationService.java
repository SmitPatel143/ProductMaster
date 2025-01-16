package com.example.productmaster.Service;

import com.example.productmaster.DTO.ApiResponse;
import com.example.productmaster.DTO.RegisterUser;
import com.example.productmaster.Entity.ConfirmationToken;
import com.example.productmaster.Entity.MyUser;
import com.example.productmaster.Exception.ConfirmationTokenExpiredException;
import com.example.productmaster.Repo.ConfirmationTokenRepo;
import com.example.productmaster.Repo.RoleRepo;
import com.example.productmaster.Repo.UserRepo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserRepo userRepo;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepo roleRepo;
    private final EmailService emailService;
    private final ConfirmationTokenRepo tokenRepo;

    public ResponseEntity<ApiResponse<String>> registerNewUserAccount(RegisterUser user, HttpServletRequest request) {

        Optional<MyUser> existingUser = userRepo.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            MyUser alreadyRegisteredUser = existingUser.get();
            if (alreadyRegisteredUser.isEnabled()) {
                log.warn("{} is already registered.", alreadyRegisteredUser.getEmail());
                return new ResponseEntity<>(setApiResponse(HttpStatus.CONFLICT.value(), "User already registered and confirmed.", null), HttpStatus.CONFLICT);
            }
            return resendConfirmationEmail(alreadyRegisteredUser, request);
        }
        MyUser currentUser = new MyUser(user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                passwordEncoder.encode(user.getPassword()),
                Set.of(roleRepo.findByName("ROLE_USER")));
        userRepo.save(currentUser);
        return new ResponseEntity<>(setApiResponse(HttpStatus.CREATED.value(), sendingConfirmationEmail(currentUser, request), null), HttpStatus.CREATED);
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

    private ResponseEntity<ApiResponse<String>> resendConfirmationEmail(MyUser user, HttpServletRequest request) {
        ConfirmationToken existingToken = tokenRepo.getByUserId(user.getId());

        if (existingToken != null && existingToken.getExpiresAt().isAfter(LocalDateTime.now()))
            return new ResponseEntity<>(setApiResponse(HttpStatus.CONFLICT.value(), "A confirmation email has already been sent to your email address. Wait for 24 Hours to resend the mail", null), HttpStatus.CONFLICT);

        if (existingToken != null)
            tokenRepo.deleteByUser(user);

        ConfirmationToken newToken = creatingConfirmationToken(user);
        tokenRepo.save(newToken);
        sendingMail(newToken, request, user.getEmail());
        return new ResponseEntity<>(setApiResponse(HttpStatus.CREATED.value(), "User has been registered successfully, Please check your email to confirm your account", null), HttpStatus.CREATED);
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

    private <T> ApiResponse<T> setApiResponse(final int value, final String message, final T data) {
        return new ApiResponse<>(value, message, data);
    }
}
