package org.example.dto;


import org.example.model.Category;

public enum CategoryDto {
    BREAKFAST, LUNCH, DINNER;

    public static CategoryDto fromCategory(Category category) {
        return switch (category) {
            case BREAKFAST -> BREAKFAST;
            case LUNCH -> LUNCH;
            case DINNER -> DINNER;
            default -> throw new IllegalArgumentException("Unsupported category: " + category);
        };
    }
}
