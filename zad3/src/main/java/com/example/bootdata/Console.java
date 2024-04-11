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
        role.setName("BasicUser");

        Set<RoleDto> roles = new HashSet<>();
        roles.add(role);
        u.setRoles(roles);

        userService.createUser(u);

        UserDto a = new UserDto();
        a.setLogin("admin"+System.currentTimeMillis());
        a.setName("adminname");
        a.setPassword("adminpass");
        a.setEmail("adminemail");
        a.setEnabled(true);

        RoleDto role2 = new RoleDto();
        role2.setName("BasicUser");

        RoleDto role3 = new RoleDto();
        role3.setName("Admin");

        Set<RoleDto> roles2 = new HashSet<>();
        roles2.add(role2);
        roles2.add(role3);
        a.setRoles(roles2);

        userService.createUser(a);

        UserDto m = new UserDto();
        m.setLogin("manager"+System.currentTimeMillis());
        m.setName("managername");
        m.setPassword("managerpass");
        m.setEmail("manageremail");
        m.setEnabled(true);

        RoleDto role4 = new RoleDto();
        role4.setName("BasicUser");

        RoleDto role5 = new RoleDto();
        role5.setName("Manager");

        Set<RoleDto> roles3 = new HashSet<>();
        roles3.add(role4);
        roles3.add(role5);
        m.setRoles(roles3);

        userService.createUser(m);

        userService.getAllUsers().forEach(System.out::println);

    }
}
