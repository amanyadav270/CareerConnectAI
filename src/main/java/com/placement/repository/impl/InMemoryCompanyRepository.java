package com.placement.repository.impl;

import com.placement.model.Company;
import com.placement.repository.CompanyRepository;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryCompanyRepository implements CompanyRepository {
    private final ConcurrentHashMap<String, Company> store = new ConcurrentHashMap<>();

    @Override
    public void save(Company company) {
        store.put(company.getId(), company);
    }

    @Override
    public Optional<Company> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Company> findByName(String name) {
        return store.values().stream()
                .filter(c -> c.getName() != null && c.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    @Override
    public List<Company> findAll() {
        return new ArrayList<>(store.values());
    }
}
