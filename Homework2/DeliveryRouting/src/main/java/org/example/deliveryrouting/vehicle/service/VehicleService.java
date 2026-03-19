package org.example.deliveryrouting.vehicle.service;

import lombok.RequiredArgsConstructor;
import org.example.deliveryrouting.vehicle.domain.Vehicle;
import org.example.deliveryrouting.vehicle.persistence.entity.VehicleEntity;
import org.example.deliveryrouting.vehicle.persistence.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll().stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    public Vehicle getVehicleById(Long id) {
        return vehicleRepository.findById(id)
                .map(this::mapToDomain)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
    }

    public Vehicle createVehicle(Vehicle vehicle) {
        VehicleEntity entity = mapToEntity(vehicle);
        VehicleEntity saved = vehicleRepository.save(entity);
        return mapToDomain(saved);
    }

    public Vehicle updateVehicle(Long id, Vehicle vehicle) {
        VehicleEntity entity = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        entity.setRegistrationNumber(vehicle.registrationNumber());
        entity.setCapacity(vehicle.capacity());

        VehicleEntity updated = vehicleRepository.save(entity);
        return mapToDomain(updated);
    }

    public void deleteVehicle(Long id) {
        vehicleRepository.deleteById(id);
    }

    private Vehicle mapToDomain(VehicleEntity entity) {
        return new Vehicle(
                entity.getId(),
                entity.getRegistrationNumber(),
                entity.getCapacity()
        );
    }

    private VehicleEntity mapToEntity(Vehicle domain) {
        VehicleEntity entity = new VehicleEntity();
        entity.setRegistrationNumber(domain.registrationNumber());
        entity.setCapacity(domain.capacity());
        return entity;
    }
}
