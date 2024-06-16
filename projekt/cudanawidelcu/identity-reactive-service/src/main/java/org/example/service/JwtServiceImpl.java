package org.example.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * Service class that implements JwtService.
 */
@Service
public class JwtServiceImpl implements JwtService {

    @Value("${secret}")
    private String SECRET;

    /**
     * Extracts the username from the JWT token.
     *
     * @param token the JWT token
     * @return the username extracted from the token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the expiration date from the JWT token.
     *
     * @param token the JWT token
     * @return the expiration date extracted from the token
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Checks if the JWT token is expired.
     *
     * @param token the JWT token
     * @return true if the token is expired, false otherwise
     */
    protected Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts a specific claim from the JWT token.
     *
     * @param token the JWT token
     * @param claimsResolver function to resolve the claim
     * @param <T> the type of the claim
     * @return the resolved claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from the JWT token.
     *
     * @param token the JWT token
     * @return all claims extracted from the token
     */
    protected Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    /**
     * Retrieves the signing key used to verify the JWT token.
     *
     * @return the signing key
     */
    protected Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Generates a JWT token for the given user details.
     *
     * @param userDetails the user details
     * @return the generated JWT token
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> extraClaims = new HashMap<>();

        var authorities = userDetails.getAuthorities();
        String firstAuthority = authorities.isEmpty() ? "" : authorities.iterator().next().getAuthority();

        extraClaims.put("role", firstAuthority);

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Checks if the JWT token is valid for the given user details.
     *
     * @param token the JWT token
     * @param userDetails the user details
     * @return true if the token is valid for the user details, false otherwise
     */
    public Boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Validates if the user associated with the JWT token is an admin.
     *
     * @param authorizationHeader the authorization header containing the JWT token
     * @return true if the user is an admin, false otherwise
     */
    public boolean validateAdmin(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return false;
        }

        String token = authorizationHeader.substring(7);
        String role = extractRole(token);

        return Objects.equals(role, "ADMIN");
    }

    /**
     * Finds the role associated with the JWT token.
     *
     * @param authorizationHeader the authorization header containing the JWT token
     * @return the role associated with the token
     */
    public String findRole(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return "";
        }

        String token = authorizationHeader.substring(7);
        return extractRole(token);
    }

    /**
     * Extracts the role claim from the JWT token.
     *
     * @param token the JWT token
     * @return the role claim extracted from the token
     */
    public String extractRole(String token) {
        return extractClaim(token, c -> (String) c.get("role"));
    }
}