package org.example.util;

import org.example.model.User;
import org.example.response.RoleResponse;
import org.example.response.UserResponse;

public class UserMapper {
    public static UserResponse convertUserToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .roleResponse(RoleResponse.fromRole(user.getRole()))
                .build();
    }
}
