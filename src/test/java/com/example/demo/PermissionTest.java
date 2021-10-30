package com.example.demo;

import org.junit.jupiter.api.Test;

public class PermissionTest {
    @Test
    public void print_enum_directly() {
        System.out.println(Permission.READ);
        System.out.println(Permission.READ.name());
        System.out.println(Permission.READ.getDescription());
    }
}
