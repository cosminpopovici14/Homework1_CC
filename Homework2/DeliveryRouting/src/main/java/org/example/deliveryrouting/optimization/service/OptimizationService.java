package org.example.deliveryrouting.optimization.service;

import lombok.RequiredArgsConstructor;
import org.example.deliveryrouting.customer.domain.Customer;
import org.example.deliveryrouting.customer.service.CustomerService;
import org.example.deliveryrouting.optimization.api.dto.RouteResponseDto;
import org.example.deliveryrouting.optimization.integration.PythonOptimizationClient;
import org.example.deliveryrouting.optimization.integration.dto.PythonOptimizationRequest;
import org.example.deliveryrouting.optimization.integration.dto.PythonOptimizationRequest.CustomerDto;
import org.example.deliveryrouting.optimization.integration.dto.PythonOptimizationRequest.DepotDto;
import org.example.deliveryrouting.optimization.integration.dto.PythonOptimizationRequest.VehicleDto;
import org.example.deliveryrouting.vehicle.domain.Vehicle;
import org.example.deliveryrouting.vehicle.service.VehicleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OptimizationService {

    private final CustomerService customerService;
    private final VehicleService vehicleService;
    private final PythonOptimizationClient optimizationClient;

    public RouteResponseDto solve() {
        List<Customer> allCustomers = customerService.getAllCustomers();
        List<Vehicle> allVehicles = vehicleService.getAllVehicles();

        if (allVehicles.isEmpty()) {
            throw new RuntimeException("Nu exisă mașini configurate. Te rog adaugă o mașină mai întâi.");
        }
        if (allCustomers.isEmpty()) {
            throw new RuntimeException("Nu există clienți configurați. Te rog adaugă clienți mai întâi.");
        }

        Vehicle vehicle = allVehicles.get(0);

        DepotDto depotDto = new DepotDto("Depozit Central", 44.43225, 26.10626);

        VehicleDto vehicleDto = new VehicleDto(vehicle.registrationNumber(), vehicle.capacity());

        List<CustomerDto> customerDtos = allCustomers.stream()
                .map(c -> new CustomerDto(
                        String.valueOf(c.id()),
                        c.name(),
                        c.coordinates().latitude(),
                        c.coordinates().longitude(),
                        c.demand()
                )).collect(Collectors.toList());

        PythonOptimizationRequest request = new PythonOptimizationRequest(depotDto, vehicleDto, customerDtos);

        return optimizationClient.solveRoute(request);
    }
}
