package org.example.service;

import jakarta.annotation.PostConstruct;
import org.example.dto.ValidateAdminDto;
import org.example.request.AuthenticationRequest;
import org.example.request.RegisterRequest;
import org.example.request.ValidateAdminRequest;
import org.example.response.AuthenticationResponse;
import org.example.response.ValidateAdminResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;

@Service
public class IdentityService {
    private final RestTemplate restTemplate;
    private final String IDENTITY_SERVICE_URL = "http://APPLICATION-GATEWAY/identity-service";

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

    public ValidateAdminResponse validateAdmin(ValidateAdminRequest validateAdminRequest, String auth) throws RuntimeException {
        ValidateAdminResponse validateAdminResponse = null;

        JSONObject validateAdminJsonObject = new JSONObject();
        try {
            validateAdminJsonObject.put("token", validateAdminRequest.getToken());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(auth.substring(7));
            HttpEntity<String> requestEntity = new HttpEntity<>(validateAdminJsonObject.toString(), headers);


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
}
