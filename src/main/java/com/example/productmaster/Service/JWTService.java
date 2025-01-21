package com.example.productmaster.Service;

import com.example.productmaster.Entity.MyUser;
import com.example.productmaster.Entity.Role;
import com.example.productmaster.Exception.TokenExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
@Data
public class JWTService {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("360000000")
    private long jwtExpirationTime;

    public SecretKey getSecretKey() {
        byte[] encodedKey = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(encodedKey);
    }

    @SuppressWarnings("unchecked")
    public Set<String> extractRoles(String token) {
        ArrayList<String> roles = extractClaim(token, claims -> claims.get("roles", ArrayList.class));
        return roles != null ? new HashSet<>(roles) : new HashSet<>();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(MyUser user) {
        Map<String, Object> claims = new HashMap<>();
        List<String> rolesNames = user.getRoles().stream()
                        .map(Role::getName)
                                .toList();
        claims.put("roles", rolesNames);
        claims.put("userId", user.getId());
        return generateToken(claims, user);
    }

    public String generateToken(Map<String, Object> extractClaims, UserDetails user) {
        return buildToken(extractClaims, user, jwtExpirationTime);
    }

    private String buildToken(Map<String, Object> claims, UserDetails user, long expirationTime) {
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .and()
                .signWith(getSecretKey())
                .compact();
    }

    public boolean isTokenValid(String token, MyUser user) {
        final String username = extractUsername(token);
        final Set<String> tokenRoles = extractRoles(token);

        List<String> userRoles = user.getRoles().stream()
                .map(Role::getName)
                .toList();
        return username.equals(user.getEmail()) &&
                new HashSet<>(tokenRoles).containsAll(userRoles) &&
                new HashSet<>(userRoles).containsAll(tokenRoles) &&
                !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException("Session expired, Please login again");
        } catch (Exception e) {
            throw new RuntimeException("Error parsing JWT token: " + e.getMessage());
        }

    }

}
