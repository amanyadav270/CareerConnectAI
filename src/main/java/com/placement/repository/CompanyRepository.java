package com.placement.repository;

import com.placement.model.Company;
import java.util.List;
import java.util.Optional;

public interface CompanyRepository {
    void save(Company company);
    Optional<Company> findById(String id);
    Optional<Company> findByName(String name);
    List<Company> findAll();
}
