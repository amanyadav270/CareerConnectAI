package com.placement.repository;

import com.placement.model.Company;
import java.util.List;
import java.util.Optional;

public interface company_repository {
    void save(Company company);
    Optional<Company> findById(String id);
    List<Company> findAll();
}