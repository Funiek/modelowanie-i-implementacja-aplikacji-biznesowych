package pl.cudanawidelcu.microservices.recipes.util;

import pl.cudanawidelcu.microservices.recipes.dto.RoleDto;
import pl.cudanawidelcu.microservices.recipes.dto.UserDto;
import pl.cudanawidelcu.microservices.recipes.model.Role;
import pl.cudanawidelcu.microservices.recipes.model.User;

import java.util.HashSet;
import java.util.Set;

public class UserMapper {
    public static Role convertRoleDtoToRole(RoleDto roleDto) {
        Role role = new Role();
        role.setName(roleDto.getName());
        return role;
    }

    public static RoleDto convertRoleToRoleDto(Role role) {
        RoleDto roleDto = new RoleDto();
        roleDto.setName(role.getName());
        return roleDto;
    }

    public static Set<RoleDto> convertRoleSetToRoleDtoSet(Set<Role> roles) {
        Set<RoleDto> roleDtoSet = new HashSet<>();
        for (Role role : roles) {
            roleDtoSet.add(UserMapper.convertRoleToRoleDto(role));
        }
        return roleDtoSet;
    }


    public static UserDto convertUserToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setLogin(user.getLogin());
        userDto.setPassword(user.getPassword());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setEnabled(user.isEnabled());

        Set<Role> roles = user.getRoles();
        Set<RoleDto> roleDto = convertRoleSetToRoleDtoSet(roles);
        userDto.setRoles(roleDto);
        return userDto;
    }

    public static Set<Role> convertRoleDtoSetToRoleSet(Set<RoleDto> rolesDto) {
        Set<Role> roleSet = new HashSet<>();
        for (RoleDto roleDto : rolesDto) {
            roleSet.add(UserMapper.convertRoleDtoToRole(roleDto));
        }
        return roleSet;
    }
}
