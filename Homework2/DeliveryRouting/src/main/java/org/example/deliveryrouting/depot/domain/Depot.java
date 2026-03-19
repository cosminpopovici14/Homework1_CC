package org.example.deliveryrouting.depot.domain;

import org.example.deliveryrouting.common.domain.Coordinates;

public record Depot(
        Long id,
        String address,
        Coordinates coordinates
) {}
