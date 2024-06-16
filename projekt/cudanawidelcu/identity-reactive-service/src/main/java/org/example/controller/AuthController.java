package org.example.controller;

import org.example.request.AuthenticationRequest;
import org.example.request.RegisterRequest;
import org.example.response.AuthenticationResponse;
import org.example.response.RoleResponse;
import org.example.response.ValidateAdminResponse;
import org.example.service.AuthenticationService;
import org.example.service.AuthenticationServiceImpl;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.config.EnableWebFlux;
import reactor.core.publisher.Mono;

/**
 * Controller class for handling authentication and authorization endpoints.
 */
@RestController
@RequestMapping("/api/v1/auth")
@EnableWebFlux
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class AuthController {

    private final AuthenticationService authenticationService;

    /**
     * Constructor for AuthController.
     *
     * @param authenticationService the authentication service to use for handling requests
     */
    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Endpoint for user registration.
     *
     * @param request the registration request body
     * @return Mono emitting AuthenticationResponse
     */
    @PostMapping("/register")
    public Mono<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return authenticationService.register(request);
    }

    /**
     * Endpoint for user authentication.
     *
     * @param request the authentication request body
     * @return Mono emitting AuthenticationResponse
     */
    @PostMapping("/authenticate")
    public Mono<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return authenticationService.authenticate(request);
    }

    /**
     * Endpoint for validating admin status.
     *
     * @param auth the Authorization header containing the JWT token
     * @return Mono emitting ValidateAdminResponse
     */
    @PostMapping("/validate-admin")
    public Mono<ValidateAdminResponse> validateAdmin(@RequestHeader("Authorization") String auth) {
        return authenticationService.validateAdmin(auth);
    }

    /**
     * Endpoint for finding user's role.
     *
     * @param auth the Authorization header containing the JWT token
     * @return Mono emitting RoleResponse
     */
    @PostMapping("/role")
    public Mono<RoleResponse> findRole(@RequestHeader("Authorization") String auth) {
        return authenticationService.findRole(auth);
    }
}
