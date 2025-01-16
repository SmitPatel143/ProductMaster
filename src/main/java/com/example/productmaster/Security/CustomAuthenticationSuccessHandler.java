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
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private final JWTService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        MyUser user = (MyUser) authentication.getPrincipal();
        String token = jwtService.generateToken(user);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(
                "{\"" + HttpHeaders.AUTHORIZATION + "\":\"" + "Bearer " + token + "\"}"
        );
        logger.info(token);
        addWelcomeCookie(getUsername(authentication), response);

//        String targetUrl = determineTargetUrl(authentication);
//        redirectStrategy.sendRedirect(request, response, targetUrl);
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

    private String getUsername(final Authentication authentication) {
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
