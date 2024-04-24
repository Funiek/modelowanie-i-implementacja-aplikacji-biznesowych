package com.example.bootdata.service;

import com.example.bootdata.dto.UserDto;
import com.example.bootdata.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void createUser(UserDto user);
    List<UserDto> getAllUsers();
    void deleteUserById(Long id);
    Optional<UserDto> findById(Long id);
}
