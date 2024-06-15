package org.example.util;

import org.example.model.User;
import org.example.model.UserRole;
import org.example.request.RoleRequest;
import org.example.request.UserRequest;
import org.example.response.UserResponse;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
class UserMapperTest {

    @Test
    void convertUserToUserResponse_ShouldReturnUserResponse() {
        // Given
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .userRole(UserRole.ADMIN)
                .build();

        // When
        UserResponse userResponse = UserMapper.convertUserToUserResponse(user);

        // Then
        assertThat(userResponse).isNotNull();
        assertThat(userResponse.getId()).isEqualTo(user.getId());
        assertThat(userResponse.getUsername()).isEqualTo(user.getUsername());
        assertThat(userResponse.getRoleResponse()).isNotNull();
        assertThat(userResponse.getRoleResponse().name()).isEqualTo(user.getUserRole().name());
    }

    @Test
    void convertUserRequestToUser_ShouldReturnUser() {
        // Given
        UserRequest userRequest = UserRequest.builder()
                .id(1L)
                .username("testuser")
                .roleRequest(RoleRequest.USER)
                .build();

        // When
        User user = UserMapper.convertUserRequestToUser(userRequest);

        // Then
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(userRequest.getId());
        assertThat(user.getUsername()).isEqualTo(userRequest.getUsername());
        assertThat(user.getUserRole()).isEqualTo(UserRole.USER);
    }
}