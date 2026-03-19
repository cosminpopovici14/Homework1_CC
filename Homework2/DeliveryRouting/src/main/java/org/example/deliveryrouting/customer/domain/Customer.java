package org.example.deliveryrouting.customer.domain;

import org.example.deliveryrouting.common.domain.Coordinates;

public record Customer(
        Long id,
        String name,
        String addressText,
        Coordinates coordinates,
        int demand
) {}
