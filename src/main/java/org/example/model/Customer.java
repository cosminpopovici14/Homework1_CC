package org.example.model;

public record Customer(
        Long id,
        String name,
        String email,
        String phone
) {}
