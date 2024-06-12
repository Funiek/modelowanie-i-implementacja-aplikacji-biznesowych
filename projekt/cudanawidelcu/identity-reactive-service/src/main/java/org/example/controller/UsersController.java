package org.example.controller;

import org.example.exception.UserNotAdminException;
import org.example.model.User;
import org.example.request.UserRequest;
import org.example.response.UserResponse;
import org.example.response.ValidateAdminResponse;
import org.example.service.AuthenticationService;
import org.example.service.UserService;
import org.example.util.UserMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.config.EnableWebFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/users")
@EnableWebFlux
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class UsersController {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    public UsersController(AuthenticationService authenticationService, UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @GetMapping
    public Flux<UserResponse> userResponses(@RequestHeader("Authorization") String auth) {
        return authenticationService.validateAdmin(auth)
                .filter(ValidateAdminResponse::getIsValid)
                .flatMapMany(valid -> userService.getAll()
                        .map(UserMapper::convertUserToUserResponse)
                );
    }

    @GetMapping("/{id}")
    public Mono<UserResponse> findById(@PathVariable("id") Long id, @RequestHeader("Authorization") String auth) {
        return authenticationService.validateAdmin(auth)
                .filter(ValidateAdminResponse::getIsValid)
                .flatMap(valid -> userService.findById(id))
                .map(UserMapper::convertUserToUserResponse);
    }
    @PutMapping("/{id}")
    public Mono<UserResponse> update(@PathVariable("id") Long id, @RequestHeader("Authorization") String auth, @RequestBody UserRequest userRequest) {
        return authenticationService.validateAdmin(auth)
                .filter(ValidateAdminResponse::getIsValid)
                .flatMap(valid -> {
                    User user = UserMapper.convertUserRequestToUser(userRequest);
                    return userService.update(id, user);
                })
                .map(UserMapper::convertUserToUserResponse);
    }
    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable("id") Long id, @RequestHeader("Authorization") String auth) {
        return authenticationService.validateAdmin(auth)
                .filter(ValidateAdminResponse::getIsValid)
                .flatMap(valid -> userService.delete(id));
    }
}

