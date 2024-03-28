package org.example.model;

import org.example.annotation.Immutable;

public class ImmutableTestEntity {
    public int id;

    public ImmutableTestEntity(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Immutable
    public void setId(int id) throws IllegalStateException {
        this.id = id;
    }
}
