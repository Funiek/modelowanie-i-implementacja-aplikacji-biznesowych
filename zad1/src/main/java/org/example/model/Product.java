package org.example.model;

public class Product extends Entity {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Product(int id) {
        setId(id);
    }
}
