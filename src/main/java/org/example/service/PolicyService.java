package org.example.service;

import org.example.error.BadRequestException;
import org.example.error.NotFoundException;
import org.example.model.Policy;
import org.example.storage.PolicyStore;

import java.io.IOException;
import java.util.List;

public class PolicyService {
    private final PolicyStore store;

    public PolicyService(PolicyStore store) {
        this.store = store;
    }

    public List<Policy> getAll() throws IOException {
        return store.findAll();
    }

    public Policy getById(long id) throws IOException {
        return store.findById(id).orElseThrow(() -> new NotFoundException("Policy not found: " + id));
    }

    public Policy create(Policy incoming) throws IOException {
        validate(incoming);
        return store.create(incoming);
    }

    public Policy update(long id, Policy incoming) throws IOException {
        validate(incoming);
        return store.update(id, incoming)
                .orElseThrow(() -> new NotFoundException("Policy not found: " + id));
    }

    public void delete(long id) throws IOException {
        boolean removed = store.delete(id);
        if (!removed) throw new NotFoundException("Policy not found: " + id);
    }

    private void validate(Policy p) {
        if (p == null) throw new BadRequestException("Body is required");
        if (p.startDate() == null) throw new BadRequestException("startDate is required");
        if (p.endDate() == null) throw new BadRequestException("endDate is required");
        if (p.premium() == null) throw new BadRequestException("premium is required");
        if (p.endDate().isBefore(p.startDate())) throw new BadRequestException("endDate must be after startDate");
    }

}
