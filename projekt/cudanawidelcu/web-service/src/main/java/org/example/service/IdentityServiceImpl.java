package org.example.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.example.request.IdentityAuthenticateRequest;
import org.example.request.IdentityRegisterRequest;
import org.example.request.IdentityUserUpdateRequest;
import org.example.response.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Service
public class IdentityServiceImpl {
    private final RestTemplate restTemplate;
    private final String IDENTITY_SERVICE_URL = "http://APPLICATION-GATEWAY/identity-service";

    @Value("${secret}")
    private String SECRET;

    protected Logger logger = Logger.getLogger(RecipesService.class
            .getName());

    public IdentityServiceImpl(@LoadBalanced RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    public void init() {
        logger.warning("The RestTemplate request factory is " + restTemplate.getRequestFactory().getClass());
    }

    public IdentityAuthenticateResponse register(IdentityRegisterRequest identityRegisterRequest) throws RuntimeException {
        IdentityAuthenticateResponse identityAuthenticateResponse;

        JSONObject registerJsonObject = new JSONObject();
        try {
            registerJsonObject.put("username", identityRegisterRequest.getUsername());
            registerJsonObject.put("password", identityRegisterRequest.getPassword());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> requestEntity = new HttpEntity<>(registerJsonObject.toString(), headers);

            ResponseEntity<IdentityAuthenticateResponse> responseEntity = restTemplate.exchange(
                    IDENTITY_SERVICE_URL + "/api/v1/auth/register",
                    HttpMethod.POST,
                    requestEntity,
                    IdentityAuthenticateResponse.class
            );

            identityAuthenticateResponse = responseEntity.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Błąd podczas wysyłania zapytania POST: " + e.getMessage(), e);
        }

        return identityAuthenticateResponse;
    }

    public IdentityAuthenticateResponse authenticate(IdentityAuthenticateRequest identityAuthenticateRequest) throws RuntimeException {
        IdentityAuthenticateResponse identityAuthenticateResponse;

        JSONObject registerJsonObject = new JSONObject();
        try {
            registerJsonObject.put("username", identityAuthenticateRequest.getUsername());
            registerJsonObject.put("password", identityAuthenticateRequest.getPassword());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> requestEntity = new HttpEntity<>(registerJsonObject.toString(), headers);

            ResponseEntity<IdentityAuthenticateResponse> responseEntity = restTemplate.exchange(
                    IDENTITY_SERVICE_URL + "/api/v1/auth/authenticate",
                    HttpMethod.POST,
                    requestEntity,
                    IdentityAuthenticateResponse.class
            );

            identityAuthenticateResponse = responseEntity.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Błąd podczas wysyłania zapytania POST: " + e.getMessage(), e);
        }

        return identityAuthenticateResponse;
    }

    public IdentityValidateAdminResponse validateAdmin(String token) throws RuntimeException {
        IdentityValidateAdminResponse identityValidateAdminResponse;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);


            ResponseEntity<IdentityValidateAdminResponse> responseEntity = restTemplate.exchange(
                    IDENTITY_SERVICE_URL + "/api/v1/auth/validate-admin",
                    HttpMethod.POST,
                    requestEntity,
                    IdentityValidateAdminResponse.class
            );

            identityValidateAdminResponse = responseEntity.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Błąd podczas wysyłania zapytania POST: " + e.getMessage(), e);
        }

        return identityValidateAdminResponse;
    }

    public void delete(Long id, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        restTemplate.exchange(
                IDENTITY_SERVICE_URL + "/api/v1/users/" + id,
                HttpMethod.DELETE,
                requestEntity,
                IdentityValidateAdminResponse.class
        );
    }

    public List<IdentityUserResponse> getAll(String token) {
        IdentityUserResponse[] userDtos;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<IdentityUserResponse[]> response = restTemplate.exchange(
                IDENTITY_SERVICE_URL + "/api/v1/users",
                HttpMethod.GET,
                requestEntity,
                IdentityUserResponse[].class
        );

        userDtos = response.getBody();
        return (userDtos == null || userDtos.length == 0) ? new ArrayList<>() : Arrays.asList(userDtos);
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

    public IdentityUserResponse findById(String token, Long id) {
        IdentityUserResponse userResponse;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<IdentityUserResponse> response = restTemplate.exchange(
                IDENTITY_SERVICE_URL + "/api/v1/users/" + id,
                HttpMethod.GET,
                requestEntity,
                IdentityUserResponse.class
        );

        userResponse = response.getBody();

        return userResponse;
    }

    public IdentityUserResponse update(Long id, String jwtToken, IdentityUserUpdateRequest userUpdateRequest) {
        IdentityUserResponse recipe;

        JSONObject updateUserJsonObject = new JSONObject();
        try {
            updateUserJsonObject.put("id", userUpdateRequest.getId());
            updateUserJsonObject.put("username", userUpdateRequest.getUsername());
            updateUserJsonObject.put("roleRequest", userUpdateRequest.getRoleRequest().name());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(updateUserJsonObject.toString(), headers);

        ResponseEntity<IdentityUserResponse> responseEntity = restTemplate.exchange(
                IDENTITY_SERVICE_URL + "/api/v1/users/" + id,
                HttpMethod.PUT,
                requestEntity,
                IdentityUserResponse.class
        );

        recipe = responseEntity.getBody();

        return recipe;
    }
}
