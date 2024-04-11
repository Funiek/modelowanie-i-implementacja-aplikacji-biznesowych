package com.example.bootdata;

import com.example.bootdata.dto.RoleDto;
import com.example.bootdata.dto.UserDto;
import com.example.bootdata.model.Role;
import com.example.bootdata.model.User;
import com.example.bootdata.repository.UserRepository;
import com.example.bootdata.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@Profile("!test")
public class Console implements CommandLineRunner {
    private final UserService userService;

    public Console(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        UserDto u = new UserDto();
        u.setLogin("abc"+System.currentTimeMillis());
        u.setName("name");
        u.setPassword("pass");
        u.setEmail("email");
        u.setEnabled(true);

        RoleDto role = new RoleDto();
        role.setName("Admin2");

        Set<RoleDto> roles = new HashSet<>();
        roles.add(role);
        u.setRoles(roles);

        userService.createUser(u);
        userService.getAllUsers().forEach(System.out::println);

    }
}
