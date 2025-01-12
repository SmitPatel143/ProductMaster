package com.example.productmaster.Security;

import com.example.productmaster.Entity.MyUser;
import com.example.productmaster.Service.JWTService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.logging.LoggersEndpoint;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MyCustomLoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(MyCustomLoginAuthenticationSuccessHandler.class);

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private final ActiveUserStore activeUserStore;
    private final JWTService jwtService;
    private final LoggersEndpoint loggersEndpoint;

    public MyCustomLoginAuthenticationSuccessHandler(ActiveUserStore activeUserStore, JWTService jwtService, LoggersEndpoint loggersEndpoint) {
        super();
        this.activeUserStore = activeUserStore;
        this.jwtService = jwtService;
        this.loggersEndpoint = loggersEndpoint;
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        MyUser user = (MyUser) authentication.getPrincipal();
        String token = jwtService.generateToken(user);
        response.addHeader("Authorization", "Bearer " + token);
        logger.info(token);

        addWelcomeCookie(getUsername(authentication), response);

        String targetUrl = determineTargetUrl(authentication);
        logger.info("Redirecting to the " + targetUrl);
        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    private String determineTargetUrl(Authentication authentication) {
        logger.info(authentication.getAuthorities().toString());
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        boolean isUser = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_USER"));

        if (isAdmin) {
            return "/admin/dashboard";
        } else if (isUser) {
            return "/homepage.html";
        } else {
            throw new IllegalStateException("User has no valid roles!");
        }
    }

    private final String getUsername(final Authentication authentication) {
        return ((MyUser) authentication.getPrincipal()).getUsername();
    }

    private void addWelcomeCookie(final String user, final HttpServletResponse response) {
        Cookie welcomeCookie = getWelcomeCookie(user);
        response.addCookie(welcomeCookie);
    }

    private Cookie getWelcomeCookie(final String user) {
        Cookie welcomeCookie = new Cookie("welcome", user);
        welcomeCookie.setMaxAge(60 * 60 * 24 * 30); // 30 days
        return welcomeCookie;
    }

}
