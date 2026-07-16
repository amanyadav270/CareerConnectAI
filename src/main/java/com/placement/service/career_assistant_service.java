package com.placement.service;

import com.placement.model.Student;
import com.placement.repository.StudentRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class career_assistant_service {

    private final chat_client ai_client;
    private final StudentRepository student_repo;

    public career_assistant_service(chat_client ai_client, StudentRepository student_repo) {
        this.ai_client = ai_client;
        this.student_repo = student_repo;
    }

    public String ask_question(String student_id, String message) {
        String system_prompt = "You are an advisory campus career assistant. Use only the verified context provided. Keep answers short.";
        String context = "No specific profile context.";

        if (student_id != null) {
            Optional<Student> student = student_repo.findById(student_id);
            if (student.isPresent()) {
                context = "Student Name: " + student.get().getName() + ", CGPA: " + student.get().getCgpa() + ", Skills: " + student.get().getSkills();
            }
        }

        String full_user_message = "Verified Context: [" + context + "] User Question: " + message;
        return ai_client.generate_response(system_prompt, full_user_message);
    }
}