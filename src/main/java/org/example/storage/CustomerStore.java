package org.example.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.Customer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CustomerStore {
    private final Path file = Paths.get("customers.json");
    private final ObjectMapper mapper;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public CustomerStore() {
        mapper = new ObjectMapper();
    }

    public List<Customer> findAll() throws IOException {
        lock.readLock().lock();
        try {
            return readAllUnsafe();
        } finally {
            lock.readLock().unlock();
        }
    }

    public Optional<Customer> findById(long id) throws IOException {
        return findAll().stream().filter(c -> c.id() != null && c.id() == id).findFirst();
    }

    public Customer create(Customer c) throws IOException {
        lock.writeLock().lock();
        try {
            List<Customer> all = readAllUnsafe();
            long nextId = all.stream().map(Customer::id).filter(Objects::nonNull).mapToLong(Long::longValue).max().orElse(0L) + 1;
            Customer created = new Customer(nextId, c.name(), c.email(), c.phone());
            all.add(created);
            writeAllUnsafe(all);
            return created;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Optional<Customer> update(long id, Customer c) throws IOException {
        lock.writeLock().lock();
        try {
            List<Customer> all = readAllUnsafe();
            for (int i = 0; i < all.size(); i++) {
                if (all.get(i).id() != null && all.get(i).id() == id) {
                    Customer updated = new Customer(id, c.name(), c.email(), c.phone());
                    all.set(i, updated);
                    writeAllUnsafe(all);
                    return Optional.of(updated);
                }
            }
            return Optional.empty();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean delete(long id) throws IOException {
        lock.writeLock().lock();
        try {
            List<Customer> all = readAllUnsafe();
            boolean removed = all.removeIf(c -> c.id() != null && c.id() == id);
            if (removed) writeAllUnsafe(all);
            return removed;
        } finally {
            lock.writeLock().unlock();
        }
    }

    private List<Customer> readAllUnsafe() throws IOException {
        if (!Files.exists(file)) {
            Files.writeString(file, "[]", StandardOpenOption.CREATE);
            return new ArrayList<>();
        }
        byte[] bytes = Files.readAllBytes(file);
        if (bytes.length == 0) return new ArrayList<>();
        return mapper.readValue(bytes, new TypeReference<List<Customer>>() {});
    }

    private void writeAllUnsafe(List<Customer> customers) throws IOException {
        byte[] bytes = mapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(customers);
        Files.write(file, bytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
}
