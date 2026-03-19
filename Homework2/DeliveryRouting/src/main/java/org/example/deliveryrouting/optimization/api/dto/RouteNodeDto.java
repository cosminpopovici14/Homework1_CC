package org.example.deliveryrouting.optimization.api.dto;

public record RouteNodeDto(
        String id,
        String name,
        double lat,
        double lng
) {}
