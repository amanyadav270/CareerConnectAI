package com.placement.controller;

import com.placement.dto.ChatRequestDto;
import com.placement.service.CareerAssistantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final CareerAssistantService aiService;

    // Surfaced in the response body per the recommended chat contract
    // (section 8.4: "model": "<configured-model>").
    @Value("${ollama.model}")
    private String configuredModel;

    public ChatController(CareerAssistantService aiService) {
        this.aiService = aiService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> askAi(@Valid @RequestBody ChatRequestDto request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String answer = aiService.askQuestion(request.getStudentId(), request.getDriveId(), request.getMessage());

            response.put("answer", answer);
            response.put("model", configuredModel);
            response.put("advisory", true);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("error", "Service Unavailable");
            response.put("message", "The AI assistant is currently offline.");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
        }
    }
}
