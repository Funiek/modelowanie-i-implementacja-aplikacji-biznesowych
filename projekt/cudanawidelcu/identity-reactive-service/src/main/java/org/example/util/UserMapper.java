package org.example.util;

import org.example.model.User;
import org.example.model.UserRole;
import org.example.request.UserRequest;
import org.example.response.RoleResponse;
import org.example.response.UserResponse;

public class UserMapper {
    public static UserResponse convertUserToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .roleResponse(RoleResponse.fromRole(user.getUserRole()))
                .build();
    }

    public static User convertUserRequestToUser(UserRequest userRequest) {
        return User.builder()
                .id(userRequest.getId())
                .username(userRequest.getUsername())
                .userRole(UserRole.valueOf(userRequest.getRoleRequest().name()))
                .build();
    }
}
