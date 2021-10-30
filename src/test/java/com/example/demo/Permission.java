package com.example.demo;

public enum Permission {
    READ("can read"),
    WRITE("can write");

    private final String description;

    Permission(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
