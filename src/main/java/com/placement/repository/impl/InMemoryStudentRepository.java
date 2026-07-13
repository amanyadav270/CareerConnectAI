package com.placement.repository.impl;

import com.placement.model.Student;
import com.placement.repository.StudentRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryStudentRepository implements StudentRepository {
    private final Map<String, Student> store = new ConcurrentHashMap<>();

    @Override
    public Student save(Student student) {
        store.put(student.getId(), student);
        return student;
    }

    @Override
    public Optional<Student> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Student> findByEmail(String email) {
        return store.values().stream()
                .filter(s -> s.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    @Override
    public List<Student> findAll() {
        return new ArrayList<>(store.values());
    }
}