package org.example.deliveryrouting.customer.service;

import lombok.RequiredArgsConstructor;
import org.example.deliveryrouting.common.domain.Coordinates;
import org.example.deliveryrouting.customer.domain.Customer;
import org.example.deliveryrouting.customer.persistence.entity.CustomerEntity;
import org.example.deliveryrouting.customer.persistence.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .map(this::mapToDomain)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    public Customer createCustomer(Customer customer) {
        CustomerEntity entity = mapToEntity(customer);
        CustomerEntity saved = customerRepository.save(entity);
        return mapToDomain(saved);
    }

    public Customer updateCustomer(Long id, Customer customer) {
        CustomerEntity entity = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        entity.setName(customer.name());
        entity.setAddressText(customer.addressText());
        entity.setLatitude(customer.coordinates().latitude());
        entity.setLongitude(customer.coordinates().longitude());
        entity.setDemand(customer.demand());

        CustomerEntity updated = customerRepository.save(entity);
        return mapToDomain(updated);
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    private Customer mapToDomain(CustomerEntity entity) {
        return new Customer(
                entity.getId(),
                entity.getName(),
                entity.getAddressText(),
                new Coordinates(entity.getLatitude(), entity.getLongitude()),
                entity.getDemand()
        );
    }

    private CustomerEntity mapToEntity(Customer domain) {
        CustomerEntity entity = new CustomerEntity();
        entity.setName(domain.name());
        entity.setAddressText(domain.addressText());
        if (domain.coordinates() != null) {
            entity.setLatitude(domain.coordinates().latitude());
            entity.setLongitude(domain.coordinates().longitude());
        }
        entity.setDemand(domain.demand());
        return entity;
    }
}
