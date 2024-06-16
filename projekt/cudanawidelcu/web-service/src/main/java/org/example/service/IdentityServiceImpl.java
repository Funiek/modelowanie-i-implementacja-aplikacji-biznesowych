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

/**
 * Implementation of IdentityService responsible for user authentication, registration, and user management operations.
 */
@Service
public class IdentityServiceImpl implements IdentityService {

    private final RestTemplate restTemplate;
    private final String IDENTITY_SERVICE_URL = "http://APPLICATION-GATEWAY/identity-service";

    @Value("${secret}")
    private String SECRET;

    protected Logger logger = Logger.getLogger(RecipesServiceImpl.class.getName());

    public IdentityServiceImpl(@LoadBalanced RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    public void init() {
        logger.warning("The RestTemplate request factory is " + restTemplate.getRequestFactory().getClass());
    }

    /**
     * Registers a new user.
     *
     * @param identityRegisterRequest request object containing username and password
     * @return response object containing authentication token
     * @throws RuntimeException if there's an error during the HTTP request
     */
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
            throw new RuntimeException("Error sending POST request: " + e.getMessage(), e);
        }

        return identityAuthenticateResponse;
    }

    /**
     * Authenticates a user.
     *
     * @param identityAuthenticateRequest request object containing username and password
     * @return response object containing authentication token
     * @throws RuntimeException if there's an error during the HTTP request
     */
    public IdentityAuthenticateResponse authenticate(IdentityAuthenticateRequest identityAuthenticateRequest) throws RuntimeException {
        IdentityAuthenticateResponse identityAuthenticateResponse;

        JSONObject authenticateJsonObject = new JSONObject();
        try {
            authenticateJsonObject.put("username", identityAuthenticateRequest.getUsername());
            authenticateJsonObject.put("password", identityAuthenticateRequest.getPassword());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> requestEntity = new HttpEntity<>(authenticateJsonObject.toString(), headers);

            ResponseEntity<IdentityAuthenticateResponse> responseEntity = restTemplate.exchange(
                    IDENTITY_SERVICE_URL + "/api/v1/auth/authenticate",
                    HttpMethod.POST,
                    requestEntity,
                    IdentityAuthenticateResponse.class
            );

            identityAuthenticateResponse = responseEntity.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Error sending POST request: " + e.getMessage(), e);
        }

        return identityAuthenticateResponse;
    }

    /**
     * Validates if the user associated with the token is an admin.
     *
     * @param token JWT token for authentication
     * @return response object containing validation result
     * @throws RuntimeException if there's an error during the HTTP request
     */
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
            throw new RuntimeException("Error sending POST request: " + e.getMessage(), e);
        }

        return identityValidateAdminResponse;
    }

    /**
     * Deletes a user by ID.
     *
     * @param id    user ID
     * @param token JWT token for authentication
     */
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

    /**
     * Retrieves all users.
     *
     * @param token JWT token for authentication
     * @return list of user responses
     */
    public List<IdentityUserResponse> getAll(String token) {
        IdentityUserResponse[] userResponses;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<IdentityUserResponse[]> response = restTemplate.exchange(
                IDENTITY_SERVICE_URL + "/api/v1/users",
                HttpMethod.GET,
                requestEntity,
                IdentityUserResponse[].class
        );

        userResponses = response.getBody();
        return (userResponses == null || userResponses.length == 0) ? new ArrayList<>() : Arrays.asList(userResponses);
    }

    /**
     * Retrieves the user by ID.
     *
     * @param token JWT token for authentication
     * @param id    user ID
     * @return user response object
     */
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

    /**
     * Updates a user by ID.
     *
     * @param id               user ID
     * @param jwtToken         JWT token for authentication
     * @param userUpdateRequest request object containing updated user details
     * @return updated user response object
     */
    public IdentityUserResponse update(Long id, String jwtToken, IdentityUserUpdateRequest userUpdateRequest) {
        IdentityUserResponse userResponse;

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

        userResponse = responseEntity.getBody();

        return userResponse;
    }

    /**
     * Retrieves the time for the cookie to expire based on the JWT token.
     *
     * @param token JWT token
     * @return time for the cookie to expire in seconds
     */
    public int getTimeForCookie(String token) {
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

    /**
     * Generates a signing key for JWT based on the secret.
     *
     * @return signing key
     */
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}