package com.placement.service;

public interface ChatClient {
    String generateResponse(String systemPrompt, String userMessage);
}
