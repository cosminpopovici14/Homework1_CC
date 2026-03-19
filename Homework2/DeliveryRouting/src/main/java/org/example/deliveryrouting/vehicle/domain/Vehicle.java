package org.example.deliveryrouting.vehicle.domain;

public record Vehicle (
        Long id,
        String registrationNumber,
        Long capacity
) {}
