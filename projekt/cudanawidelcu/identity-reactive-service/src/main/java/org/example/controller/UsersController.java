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

@RestController
@RequestMapping("/api/v1/users")
@EnableWebFlux
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class UsersController {
    private final AuthenticationService authenticationService;

    public UsersController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping
    public Flux<UserResponse> userResponses(@RequestHeader("Authorization") String auth) {
        return authenticationService.validateAdmin(auth)
                .filter(ValidateAdminResponse::getIsValid)
                .flatMapMany(valid -> authenticationService.getAll()
                        .map(UserMapper::convertUserToUserResponse)
                )
                .switchIfEmpty(Mono.error(new UserNotAdminException()));
    }

    @GetMapping("/{id}")
    public Mono<UserResponse> findById(@PathVariable("id") Long id, @RequestHeader("Authorization") String auth) {
        return authenticationService.validateAdmin(auth)
                .filter(ValidateAdminResponse::getIsValid)
                .flatMap(valid -> authenticationService.findById(id))
                .map(UserMapper::convertUserToUserResponse);
    }
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

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable("id") Long id, @RequestHeader("Authorization") String auth) {
        return authenticationService.validateAdmin(auth)
                .filter(ValidateAdminResponse::getIsValid)
                .flatMap(valid -> authenticationService.delete(id));
    }
}

