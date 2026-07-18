package com.placement.controller;

import com.placement.dto.StudentDto;
import com.placement.exception.NotFoundException;
import com.placement.model.Student;
import com.placement.repository.StudentRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentRepository studentRepo;

    public StudentController(StudentRepository studentRepo) {
        this.studentRepo = studentRepo;
    }

    // FR-01: Create a student profile. @Valid enforces NFR-03 (structural
    // validation) before any business logic runs; a duplicate email is a
    // business-rule conflict (409), not a structural error.
    @PostMapping
    public ResponseEntity<Student> createStudent(@Valid @RequestBody StudentDto payload) {
        if (studentRepo.findByEmail(payload.email).isPresent()) {
            throw new IllegalStateException("Student email already exists: " + payload.email);
        }

        Student newStudent = new Student(
            payload.id, payload.name, payload.email, payload.programme,
            payload.graduationYear, payload.cgpa, payload.activeBacklogs, payload.skills
        );

        studentRepo.save(newStudent);
        return new ResponseEntity<>(newStudent, HttpStatus.CREATED);
    }

    // FR-02: Retrieve a student by id.
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable String id) {
        Student student = studentRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Student not found: " + id));
        return ResponseEntity.ok(student);
    }

    // FR-02: Update allowed profile fields. Reuses StudentDto so the same
    // structural validation applies to updates as to creation.
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable String id, @Valid @RequestBody StudentDto payload) {
        studentRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Student not found: " + id));

        Student updatedStudent = new Student(
                id, payload.name, payload.email, payload.programme,
                payload.graduationYear, payload.cgpa, payload.activeBacklogs, payload.skills
        );

        studentRepo.save(updatedStudent);
        return ResponseEntity.ok(updatedStudent);
    }
}
