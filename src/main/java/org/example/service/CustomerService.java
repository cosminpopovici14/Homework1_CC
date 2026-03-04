package org.example.service;

import org.example.error.BadRequestException;
import org.example.error.NotFoundException;
import org.example.model.Customer;
import org.example.storage.CustomerStore;

import java.io.IOException;
import java.util.List;

public class CustomerService {
    private final CustomerStore store;

    public CustomerService(CustomerStore store) {
        this.store = store;
    }

    public List<Customer> getAll() throws IOException {
        return store.findAll();
    }

    public Customer getById(long id) throws IOException {
        return store.findById(id).orElseThrow(() -> new NotFoundException("Customer not found: " + id));
    }

    public Customer create(Customer incoming) throws IOException {
        validate(incoming);
        return store.create(incoming);
    }

    public Customer update(long id, Customer incoming) throws IOException {
        validate(incoming);
        return store.update(id, incoming)
                .orElseThrow(() -> new NotFoundException("Customer not found: " + id));
    }

    public void delete(long id) throws IOException {
        boolean removed = store.delete(id);
        if (!removed) throw new NotFoundException("Customer not found: " + id);
    }

    private void validate(Customer c) {
        if (c == null) throw new BadRequestException("Body is required");
        if (c.name() == null || c.name().isBlank()) throw new BadRequestException("name is required");
        if (c.email() == null || c.email().isBlank()) throw new BadRequestException("email is required");
    }
}
