package org.example.controller;

import org.example.exception.UserNotAdminException;
import org.example.model.User;
import org.example.request.AuthenticationRequest;
import org.example.request.RegisterRequest;
import org.example.request.ValidateAdminRequest;
import org.example.response.AuthenticationResponse;
import org.example.response.UserResponse;
import org.example.response.ValidateAdminResponse;
import org.example.service.AuthenticationService;
import org.example.service.UserService;
import org.example.security.JwtService;
import org.example.util.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class AuthController {
    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
    @PostMapping("/register")
    public AuthenticationResponse register(@RequestBody RegisterRequest request) {
        return authenticationService.register(request);
    }

    @PostMapping("/authenticate")
    public AuthenticationResponse authenticate(@RequestBody AuthenticationRequest request) {
        return authenticationService.authenticate(request);
    }

    @PostMapping("/validate-admin")
    public ValidateAdminResponse validateAdmin(@RequestHeader("Authorization") String auth) {
        return authenticationService.validateAdmin(auth);
    }

    @PostMapping("/users")
    public List<UserResponse> userResponses(@RequestHeader("Authorization") String auth) {
        ValidateAdminResponse validateAdminResponse = authenticationService.validateAdmin(auth);
        if (validateAdminResponse.getIsValid()) {
            List<User> users = authenticationService.getAll();
            return users.stream()
                    .map(UserMapper::convertUserToUserResponse)
                    .collect(Collectors.toList());
        }

        throw new UserNotAdminException();
    }
}

