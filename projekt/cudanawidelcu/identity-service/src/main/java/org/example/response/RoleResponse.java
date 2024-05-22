package org.example.response;

import org.example.model.Role;

public enum RoleResponse {
    USER, ADMIN;

    public static RoleResponse fromRole(Role role) {
        return switch (role) {
            case USER -> USER;
            case ADMIN -> ADMIN;
            default -> throw new IllegalArgumentException("Unsupported role: " + role);
        };
    }
}