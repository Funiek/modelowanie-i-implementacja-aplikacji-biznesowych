package org.example.model;

import org.example.annotation.NotNull;

public class Entity {
    @NotNull
    private String nameWithAnnotation;
    private String nameWithoutAnnotation;

    public Entity() {}
    public String getNameWithAnnotation() {
        return nameWithAnnotation;
    }

    public void setNameWithAnnotation(String nameWithAnnotation) {
        this.nameWithAnnotation = nameWithAnnotation;
    }


    public String getNameWithoutAnnotation() {
        return nameWithoutAnnotation;
    }

    public void setNameWithoutAnnotation(String nameWithoutAnnotation) {
        this.nameWithoutAnnotation = nameWithoutAnnotation;
    }
}
