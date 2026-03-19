package org.example.deliveryrouting.optimization.integration.dto;

import java.util.List;

public record PythonOptimizationRequest(
        DepotDto depot,
        VehicleDto vehicle,
        List<CustomerDto> customers
) {
    public record DepotDto(String name, double lat, double lng) {}
    public record VehicleDto(String id, Long capacity) {}
    public record CustomerDto(String id, String name, double lat, double lng, int demand) {}
}
