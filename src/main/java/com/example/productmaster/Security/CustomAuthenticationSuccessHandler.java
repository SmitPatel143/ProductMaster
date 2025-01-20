package com.example.productmaster.Security;

import com.example.productmaster.Entity.MyUser;
import com.example.productmaster.Service.JWTService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);
    private final JWTService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        MyUser user = (MyUser) authentication.getPrincipal();
        String token = jwtService.generateToken(user);

        response.addCookie(getWelcomeCookie(getUsername(authentication)));
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String jsonResponse = String.format(
                "{\"" + HttpHeaders.AUTHORIZATION + "\":\"%s\", \"username\":\"%s\"}",
                "Bearer " + token,
                getUsername(authentication)
        );
        response.getWriter().write(jsonResponse);
    }

    private String getUsername(final Authentication authentication) {
        return ((MyUser) authentication.getPrincipal()).getUsername();
    }

    private Cookie getWelcomeCookie(final String user) {
        Cookie usernameCookie = new Cookie("username", user);
        usernameCookie.setPath("/");
        usernameCookie.setHttpOnly(false);  // Allow JavaScript access
        usernameCookie.setSecure(false);    // Set to true in production with HTTPS
        usernameCookie.setMaxAge(60 * 60 * 24); // 24 hours
        return usernameCookie;
    }

}
