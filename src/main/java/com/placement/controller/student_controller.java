package com.placement.controller;

import com.placement.model.Student;
import com.placement.repository.StudentRepository;
import com.placement.dto.student_dto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/students")
public class student_controller {

    private final StudentRepository student_repo;

    public student_controller(StudentRepository student_repo) {
        this.student_repo = student_repo;
    }

    @PostMapping
    public ResponseEntity<Student> create_student(@RequestBody student_dto payload) {
        if (student_repo.findByEmail(payload.email).isPresent()) {
            throw new IllegalStateException("Student email already exists.");
        }
        
        Student new_student = new Student(
            payload.id, payload.name, payload.email, payload.programme, 
            payload.graduation_year, payload.cgpa, payload.active_backlogs, payload.skills
        );
        
        student_repo.save(new_student);
        return new ResponseEntity<>(new_student, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> get_student(@PathVariable String id) {
        Optional<Student> student = student_repo.findById(id);
        if (student.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student.get());
    }
}