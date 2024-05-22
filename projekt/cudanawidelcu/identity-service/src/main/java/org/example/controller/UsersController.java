package org.example.controller;

import org.example.exception.UserNotAdminException;
import org.example.model.User;
import org.example.response.UserResponse;
import org.example.response.ValidateAdminResponse;
import org.example.service.AuthenticationService;
import org.example.util.UserMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class UsersController {
    private final AuthenticationService authenticationService;

    public UsersController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping
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

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id, @RequestHeader("Authorization") String auth) {
        ValidateAdminResponse validateAdminResponse = authenticationService.validateAdmin(auth);
        if (validateAdminResponse.getIsValid()) {
            authenticationService.delete(id);
        }
    }
}
