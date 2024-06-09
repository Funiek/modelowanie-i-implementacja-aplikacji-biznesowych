package org.example.response;

import org.example.model.UserRole;

public enum RoleResponse {
    USER, ADMIN, NONE;

    public static RoleResponse fromRole(UserRole userRole) {
        return switch (userRole) {
            case USER -> USER;
            case ADMIN -> ADMIN;
            default -> throw new IllegalArgumentException("Unsupported role: " + userRole);
        };
    }
}