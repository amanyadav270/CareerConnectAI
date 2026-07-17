package com.placement.controller;

import com.placement.dto.chat_request_dto;
import com.placement.service.career_assistant_service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class chat_controller {

    private final career_assistant_service ai_service;

    public chat_controller(career_assistant_service ai_service) {
        this.ai_service = ai_service;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> ask_ai(@RequestBody chat_request_dto request) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Using getter methods to access the private fields
            String answer = ai_service.ask_question(request.get_student_id(), request.get_message());
            
            response.put("answer", answer);
            response.put("advisory", true);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("error", "Service Unavailable");
            response.put("message", "The AI assistant is currently offline.");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
        }
    }
}