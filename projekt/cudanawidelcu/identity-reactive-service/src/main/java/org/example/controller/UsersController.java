package org.example.controller;

import org.example.exception.UserNotAdminException;
import org.example.model.User;
import org.example.request.UserRequest;
import org.example.response.UserResponse;
import org.example.response.ValidateAdminResponse;
import org.example.service.AuthenticationService;
import org.example.service.AuthenticationServiceImpl;
import org.example.util.UserMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.config.EnableWebFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Controller class for handling user-related operations.
 */
@RestController
@RequestMapping("/api/v1/users")
@EnableWebFlux
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class UsersController {

    private final AuthenticationService authenticationService;

    /**
     * Constructor for UsersController.
     *
     * @param authenticationService the authentication service to use for handling requests
     */
    public UsersController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Retrieves all users.
     *
     * @param auth the Authorization header containing the JWT token
     * @return Flux emitting UserResponse for each user
     */
    @GetMapping
    public Flux<UserResponse> userResponses(@RequestHeader("Authorization") String auth) {
        return authenticationService.validateAdmin(auth)
                .filter(ValidateAdminResponse::getIsValid)
                .flatMapMany(valid -> authenticationService.getAll()
                        .map(UserMapper::convertUserToUserResponse)
                )
                .switchIfEmpty(Mono.error(new UserNotAdminException()));
    }

    /**
     * Finds a user by ID.
     *
     * @param id the ID of the user to find
     * @param auth the Authorization header containing the JWT token
     * @return Mono emitting UserResponse for the found user
     */
    @GetMapping("/{id}")
    public Mono<UserResponse> findById(@PathVariable("id") Long id, @RequestHeader("Authorization") String auth) {
        return authenticationService.validateAdmin(auth)
                .filter(ValidateAdminResponse::getIsValid)
                .flatMap(valid -> authenticationService.findById(id))
                .map(UserMapper::convertUserToUserResponse);
    }

    /**
     * Updates a user by ID.
     *
     * @param id the ID of the user to update
     * @param auth the Authorization header containing the JWT token
     * @param userRequest the updated user information
     * @return Mono emitting UserResponse for the updated user
     */
    @PutMapping("/{id}")
    public Mono<UserResponse> update(@PathVariable("id") Long id, @RequestHeader("Authorization") String auth, @RequestBody UserRequest userRequest) {
        return authenticationService.validateAdmin(auth)
                .filter(ValidateAdminResponse::getIsValid)
                .flatMap(valid -> {
                    User user = UserMapper.convertUserRequestToUser(userRequest);
                    return authenticationService.update(id, user);
                })
                .map(UserMapper::convertUserToUserResponse);
    }

    /**
     * Deletes a user by ID.
     *
     * @param id the ID of the user to delete
     * @param auth the Authorization header containing the JWT token
     * @return Mono emitting Void upon successful deletion
     */
    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable("id") Long id, @RequestHeader("Authorization") String auth) {
        return authenticationService.validateAdmin(auth)
                .filter(ValidateAdminResponse::getIsValid)
                .flatMap(valid -> authenticationService.delete(id));
    }
}