package org.example.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.model.Policy;

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

public class PolicyStore {
    private final Path file = Paths.get("policies.json");
    private final ObjectMapper mapper;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public PolicyStore(){
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    public List<Policy> findAll() throws IOException {
        lock.readLock().lock();
        try {
            return readAllUnsafe();
        } finally {
            lock.readLock().unlock();
        }
    }

    public Optional<Policy> findById(long id) throws IOException {
        return findAll().stream().filter(p -> p.id() != null && p.id() == id).findFirst();
    }

    public Policy create(Policy p) throws IOException {
        lock.writeLock().lock();
        try {
            List<Policy> all = readAllUnsafe();
            long nextId = all.stream().map(Policy::id).filter(Objects::nonNull).mapToLong(Long::longValue).max().orElse(0L) + 1;
            Policy created = new Policy(nextId, p.startDate(), p.endDate(), p.premium());
            all.add(created);
            writeAllUnsafe(all);
            return created;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Optional<Policy> update(long id, Policy p) throws IOException {
        lock.writeLock().lock();
        try {
            List<Policy> all = readAllUnsafe();
            for (int i = 0; i < all.size(); i++) {
                if (all.get(i).id() != null && all.get(i).id() == id) {
                    Policy updated = new Policy(id, p.startDate(), p.endDate(), p.premium());
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
            List<Policy> all = readAllUnsafe();
            boolean removed = all.removeIf(p -> p.id() != null && p.id() == id);
            if (removed) writeAllUnsafe(all);
            return removed;
        } finally {
            lock.writeLock().unlock();
        }
    }


    private List<Policy> readAllUnsafe() throws IOException {
        if (!Files.exists(file)) {
            Files.writeString(file, "[]", StandardOpenOption.CREATE);
            return new ArrayList<>();
        }
        byte[] bytes = Files.readAllBytes(file);
        if (bytes.length == 0) return new ArrayList<>();
        return mapper.readValue(bytes, new TypeReference<List<Policy>>() {});
    }

    private void writeAllUnsafe(List<Policy> policies) throws IOException {
        byte[] bytes = mapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(policies);
        Files.write(file, bytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
}
