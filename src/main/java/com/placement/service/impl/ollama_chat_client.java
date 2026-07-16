package com.placement.service.impl;

import com.placement.service.chat_client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ollama_chat_client implements chat_client {

    @Value("${ollama.base-url}")
    private String base_url;

    @Value("${ollama.model}")
    private String model;

    private final RestTemplate rest_template;

    public ollama_chat_client() {
        this.rest_template = new RestTemplate();
    }

    @Override
    public String generate_response(String system_prompt, String user_message) {
        try {
            String url = base_url + "/api/chat";
            Map<String, Object> request = new HashMap<>();
            request.put("model", model);
            request.put("stream", false);
            
            // Simplified Ollama Request Shape
            request.put("messages", List.of(
                Map.of("role", "system", "content", system_prompt),
                Map.of("role", "user", "content", user_message)
            ));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            ResponseEntity<Map> response = rest_template.postForEntity(url, entity, Map.class);
            Map<String, Object> body = response.getBody();
            
            if (body != null && body.containsKey("message")) {
                Map<String, String> msg = (Map<String, String>) body.get("message");
                return msg.get("content");
            }
            return "The assistant could not generate a response.";
        } catch (ResourceAccessException e) {
            // Triggers if Ollama is turned off or times out
            throw new RuntimeException("Ollama service unavailable");
        }
    }
}