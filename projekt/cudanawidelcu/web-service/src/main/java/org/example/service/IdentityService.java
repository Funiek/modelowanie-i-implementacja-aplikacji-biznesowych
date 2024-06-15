package org.example.service;

import org.example.request.IdentityAuthenticateRequest;
import org.example.request.IdentityRegisterRequest;
import org.example.request.IdentityUserUpdateRequest;
import org.example.response.IdentityAuthenticateResponse;
import org.example.response.IdentityUserResponse;
import org.example.response.IdentityValidateAdminResponse;

import java.util.List;

public interface IdentityService {
    IdentityAuthenticateResponse register(IdentityRegisterRequest identityRegisterRequest) throws RuntimeException;

    IdentityAuthenticateResponse authenticate(IdentityAuthenticateRequest identityAuthenticateRequest) throws RuntimeException;

    IdentityValidateAdminResponse validateAdmin(String token) throws RuntimeException;

    void delete(Long id, String token);

    List<IdentityUserResponse> getAll(String token);

    int getTimeForCookie(String token);

    IdentityUserResponse findById(String token, Long id);

    IdentityUserResponse update(Long id, String jwtToken, IdentityUserUpdateRequest userUpdateRequest);
}