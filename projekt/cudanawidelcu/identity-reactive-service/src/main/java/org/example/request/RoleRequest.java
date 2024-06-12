package org.example.request;

import org.example.model.UserRole;

public enum RoleRequest {
    USER, ADMIN, NONE;

    public static RoleRequest fromRole(UserRole userRole) {
        return switch (userRole) {
            case USER -> USER;
            case ADMIN -> ADMIN;
            default -> throw new IllegalArgumentException("Unsupported role: " + userRole);
        };
    }
}