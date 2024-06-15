package org.example.service;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.function.Function;
import java.util.Date;

public interface JwtService {

    String extractUsername(String token);

    Date extractExpiration(String token);

    String extractRole(String token);

    <T> T extractClaim(String token, Function<io.jsonwebtoken.Claims, T> claimsResolver);

    Boolean isTokenValid(String token, UserDetails userDetails);

    boolean validateAdmin(String authorizationHeader);

    String findRole(String authorizationHeader);

    String generateToken(UserDetails userDetails);
}