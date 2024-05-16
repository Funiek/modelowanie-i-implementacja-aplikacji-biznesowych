package org.example.model;

import org.example.dto.CategoryDto;

public enum Category {
    BREAKFAST, LUNCH, DINNER;

    public static Category fromCategoryDto(CategoryDto categoryDto) {
        return switch (categoryDto) {
            case BREAKFAST -> BREAKFAST;
            case LUNCH -> LUNCH;
            case DINNER -> DINNER;
            default -> throw new IllegalArgumentException("Unsupported category: " + categoryDto);
        };
    }
}
