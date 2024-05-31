package org.example.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.example.request.AuthenticationRequest;
import org.example.request.RegisterRequest;
import org.example.response.AuthenticationResponse;
import org.example.response.UserResponse;
import org.example.response.ValidateAdminResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Service
public class IdentityService {
    private final RestTemplate restTemplate;
    private final String IDENTITY_SERVICE_URL = "http://APPLICATION-GATEWAY/identity-service";

    @Value("${secret}")
    private String SECRET;

    protected Logger logger = Logger.getLogger(RecipesService.class
            .getName());

    public IdentityService(@LoadBalanced RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    public void init() {
        logger.warning("The RestTemplate request factory is " + restTemplate.getRequestFactory().getClass());
    }

    public AuthenticationResponse register(RegisterRequest registerRequest) throws RuntimeException {
        AuthenticationResponse authenticationResponse = null;

        JSONObject registerJsonObject = new JSONObject();
        try {
            registerJsonObject.put("username", registerRequest.getUsername());
            registerJsonObject.put("password", registerRequest.getPassword());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> requestEntity = new HttpEntity<>(registerJsonObject.toString(), headers);

            ResponseEntity<AuthenticationResponse> responseEntity = restTemplate.exchange(
                    IDENTITY_SERVICE_URL + "/api/v1/auth/register",
                    HttpMethod.POST,
                    requestEntity,
                    AuthenticationResponse.class
            );

            authenticationResponse = responseEntity.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Błąd podczas wysyłania zapytania POST: " + e.getMessage(), e);
        }

        return authenticationResponse;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) throws RuntimeException {
        AuthenticationResponse authenticationResponse = null;

        JSONObject registerJsonObject = new JSONObject();
        try {
            registerJsonObject.put("username", authenticationRequest.getUsername());
            registerJsonObject.put("password", authenticationRequest.getPassword());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> requestEntity = new HttpEntity<>(registerJsonObject.toString(), headers);

            ResponseEntity<AuthenticationResponse> responseEntity = restTemplate.exchange(
                    IDENTITY_SERVICE_URL + "/api/v1/auth/authenticate",
                    HttpMethod.POST,
                    requestEntity,
                    AuthenticationResponse.class
            );

            authenticationResponse = responseEntity.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Błąd podczas wysyłania zapytania POST: " + e.getMessage(), e);
        }

        return authenticationResponse;
    }

    public ValidateAdminResponse validateAdmin(String token) throws RuntimeException {
        ValidateAdminResponse validateAdminResponse = null;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);


            ResponseEntity<ValidateAdminResponse> responseEntity = restTemplate.exchange(
                    IDENTITY_SERVICE_URL + "/api/v1/auth/validate-admin",
                    HttpMethod.POST,
                    requestEntity,
                    ValidateAdminResponse.class
            );

            validateAdminResponse = responseEntity.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Błąd podczas wysyłania zapytania POST: " + e.getMessage(), e);
        }

        return validateAdminResponse;
    }

    public void delete(Long id, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        restTemplate.exchange(
                IDENTITY_SERVICE_URL + "/api/v1/users/" + id,
                HttpMethod.DELETE,
                requestEntity,
                ValidateAdminResponse.class
        );
    }

    public List<UserResponse> getAll(String token) {
        UserResponse[] userDtos = null;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<UserResponse[]> response = restTemplate.exchange(
                IDENTITY_SERVICE_URL + "/api/v1/users",
                HttpMethod.GET,
                requestEntity,
                UserResponse[].class
        );

        userDtos = response.getBody();
        return (userDtos == null || userDtos.length == 0) ? null : Arrays.asList(userDtos);
    }
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public int getTimeForCookie(String token) {
        Keys.secretKeyFor(SignatureAlgorithm.HS256);
        Key key = getSigningKey();
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Date issuedAt = claims.getIssuedAt();
        Date expiration = claims.getExpiration();

        return (int) (expiration.getTime() - issuedAt.getTime()) / 1000;
    }
}
