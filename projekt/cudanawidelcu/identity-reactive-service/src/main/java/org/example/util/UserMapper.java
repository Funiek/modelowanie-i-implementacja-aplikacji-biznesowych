package org.example.util;

import org.example.model.User;
import org.example.model.UserRole;
import org.example.request.UserRequest;
import org.example.response.RoleResponse;
import org.example.response.UserResponse;

/**
 * Utility class for mapping User and related objects.
 */
public class UserMapper {

    /**
     * Converts a User object to a UserResponse object.
     *
     * @param user the User object to convert
     * @return a UserResponse object
     */
    public static UserResponse convertUserToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .roleResponse(RoleResponse.fromRole(user.getUserRole()))
                .build();
    }

    /**
     * Converts a UserRequest object to a User object.
     *
     * @param userRequest the UserRequest object to convert
     * @return a User object
     */
    public static User convertUserRequestToUser(UserRequest userRequest) {
        return User.builder()
                .id(userRequest.getId())
                .username(userRequest.getUsername())
                .userRole(UserRole.valueOf(userRequest.getRoleRequest().name()))
                .build();
    }
}