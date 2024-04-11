package com.example.bootdata.service;

import com.example.bootdata.dto.RoleDto;
import com.example.bootdata.dto.UserDto;
import com.example.bootdata.model.Role;
import com.example.bootdata.model.User;
import com.example.bootdata.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordServiceImpl passwordService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordServiceImpl passwordService) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
    }

    @Override
    @Transactional
    public void createUser(UserDto dto) {
        User user = new User();

        user.setLogin(dto.getLogin());
        user.setPassword(passwordService.hashPassword(dto.getPassword()));
        user.setName(dto.getName());

        var roles = convertRoleDtoSetToRoleSet(dto.getRoles());
        user.setRoles(roles);
        user.setEnabled(dto.isEnabled());
        user.setEmail(dto.getEmail());

        userRepository.save(user);
    }

    @Override
    @Transactional
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::convertUserToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Optional<UserDto> findById(Long id) {
        var userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UserDto userDto = convertUserToUserDto(user);
            return Optional.of(userDto);
        }
        return Optional.empty();
    }

    private UserDto convertUserToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setLogin(user.getLogin());
        userDto.setPassword(user.getPassword());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setEnabled(user.isEnabled());

        var roles = user.getRoles();
        var roleDto = convertRoleSetToRoleDtoSet(roles);
        userDto.setRoles(roleDto);
        return userDto;
    }

    private RoleDto convertRoleToRoleDto(Role role) {
        RoleDto roleDto = new RoleDto();
        roleDto.setName(role.getName());
        return roleDto;
    }

    private Role convertRoleDtoToRole(RoleDto roleDto) {
        Role role = new Role();
        role.setName(roleDto.getName());
        return role;
    }

    private Set<RoleDto> convertRoleSetToRoleDtoSet(Set<Role> roles) {
        Set<RoleDto> roleDtoSet = new HashSet<>();
        for (Role role : roles) {
            roleDtoSet.add(convertRoleToRoleDto(role));
        }
        return roleDtoSet;
    }

    private Set<Role> convertRoleDtoSetToRoleSet(Set<RoleDto> rolesDto) {
        Set<Role> roleSet = new HashSet<>();
        for (RoleDto roleDto : rolesDto) {
            roleSet.add(convertRoleDtoToRole(roleDto));
        }
        return roleSet;
    }
}
