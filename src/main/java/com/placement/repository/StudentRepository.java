package com.placement.repository;

import com.placement.model.Student;
import java.util.List;
import java.util.Optional;

public interface StudentRepository {
    Student save(Student student);
    Optional<Student> findById(String id);
    Optional<Student> findByEmail(String email);
    List<Student> findAll();
}
