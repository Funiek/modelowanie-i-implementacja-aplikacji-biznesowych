package com.example.bootdata.dto;

import com.example.bootdata.model.Role;
import lombok.Data;

import java.util.Set;

@Data
public class UserDto {
    private String login;
    private String password;
    private String name;
    private String email;
    private boolean enabled;
    private Set<RoleDto> roles;
}
