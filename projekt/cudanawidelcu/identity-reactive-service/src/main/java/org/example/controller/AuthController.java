package org.example.controller;

import org.example.request.AuthenticationRequest;
import org.example.request.RegisterRequest;
import org.example.response.AuthenticationResponse;
import org.example.response.ValidateAdminResponse;
import org.example.service.AuthenticationService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class AuthController {
    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public Mono<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return authenticationService.register(request);
    }

    @PostMapping("/authenticate")
    public Mono<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return authenticationService.authenticate(request);
    }

    @PostMapping("/validate-admin")
    public Mono<ValidateAdminResponse> validateAdmin(@RequestHeader("Authorization") String auth) {
        return authenticationService.validateAdmin(auth);
    }
}
