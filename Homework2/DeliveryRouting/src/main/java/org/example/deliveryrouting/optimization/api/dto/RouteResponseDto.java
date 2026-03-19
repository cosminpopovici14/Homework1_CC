package org.example.deliveryrouting.optimization.api.dto;

import java.util.List;

public record RouteResponseDto(
        double totalDistance,
        List<RouteNodeDto> route
) { }
