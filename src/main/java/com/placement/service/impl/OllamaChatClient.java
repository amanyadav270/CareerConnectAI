package com.placement.service.impl;

import com.placement.service.ChatClient;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OllamaChatClient implements ChatClient {

    @Value("${ollama.base-url}")
    private String baseUrl;

    @Value("${ollama.model}")
    private String model;

    // NFR-06/NFR-08: these are external configuration values and must
    // actually bound the HTTP call, not just sit unused in properties.
    @Value("${ollama.connect-timeout-seconds:3}")
    private int connectTimeoutSeconds;

    @Value("${ollama.read-timeout-seconds:60}")
    private int readTimeoutSeconds;

    private RestTemplate restTemplate;

    // Built here (rather than in the constructor) because @Value fields are
    // only populated after construction - building the timeout-aware
    // RestTemplate in the constructor would silently use 0/default timeouts.
    @PostConstruct
    private void initClient() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(connectTimeoutSeconds * 1000);
        factory.setReadTimeout(readTimeoutSeconds * 1000);
        this.restTemplate = new RestTemplate(factory);
    }

    @Override
    public String generateResponse(String systemPrompt, String userMessage) {
        try {
            String url = baseUrl + "/api/chat";
            Map<String, Object> request = new HashMap<>();
            request.put("model", model);
            request.put("stream", false);

            // Simplified Ollama Request Shape
            request.put("messages", List.of(
                Map.of("role", "system", "content", systemPrompt),
                Map.of("role", "user", "content", userMessage)
            ));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            // Using a raw Map to catch the response avoids strict generic mismatch errors
            @SuppressWarnings("rawtypes")
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

            @SuppressWarnings("rawtypes")
            Map body = response.getBody();

            if (body != null && body.containsKey("message")) {
                @SuppressWarnings("rawtypes")
                Map msg = (Map) body.get("message");
                return (String) msg.get("content");
            }

            return "The assistant could not generate a response.";
        } catch (ResourceAccessException e) {
            // Triggers if Ollama is turned off, refuses the connection, or
            // the configured connect/read timeout above is exceeded.
            throw new RuntimeException("Ollama service unavailable", e);
        }
    }
}
