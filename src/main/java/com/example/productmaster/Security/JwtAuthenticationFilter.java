package com.example.productmaster.Security;

import com.example.productmaster.Entity.MyUser;
import com.example.productmaster.Service.JWTService;
import com.example.productmaster.Service.MyUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final MyUserDetailsService userDetailsService;

    private final JWTService jwtService;

    public JwtAuthenticationFilter(JWTService jwtService, MyUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, FilterChain filterChain) throws IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        String token = null, username = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            username = jwtService.extractUsername(token);
        }

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (username != null && authentication == null) {
                logger.info("User {} logged in", username);
                logger.info(token);
                MyUser user = (MyUser) userDetailsService.loadUserByUsername(username);
                if (jwtService.isTokenValid(token, user)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
            filterChain.doFilter(request, response);
        }catch (ExpiredJwtException e){
            logger.error("Token expired, redirecting to login: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setHeader("Token-Expired", "true");
            response.sendRedirect("/login");
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 Forbidden
            response.sendRedirect("/login");
        }
    }
}
