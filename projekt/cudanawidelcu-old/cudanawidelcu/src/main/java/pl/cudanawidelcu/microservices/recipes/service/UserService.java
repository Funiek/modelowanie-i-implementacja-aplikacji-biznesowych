package pl.cudanawidelcu.microservices.recipes.service;

import pl.cudanawidelcu.microservices.recipes.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void createUser(UserDto user);
    List<UserDto> getAllUsers();
    void deleteUserById(Long id);
    Optional<UserDto> findById(Long id);
}
