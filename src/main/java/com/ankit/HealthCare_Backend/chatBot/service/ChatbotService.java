package com.ankit.HealthCare_Backend.chatBot.service;

import com.ankit.HealthCare_Backend.Exception.ChatbotServiceException;
import com.ankit.HealthCare_Backend.chatBot.dto.chatResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class ChatbotService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${groq.api.key}")
    private String apiKey;

    @Value("${groq.api.url}")
    private String apiUrl;

    @Value("${groq.model}")
    private String model;

    private static final String SYSTEM_PROMPT = """
            You are the AI Health Assistant inside a hospital management platform.
            Rules you must always follow:
            1. You may give general wellness/health information and explain how to use
               this platform (booking appointments, viewing prescriptions, etc.).
            2. You must NEVER diagnose a condition, prescribe or recommend specific
               medication/dosage, or replace a doctor's advice.
            3. If the user describes symptoms, give general guidance and clearly
               recommend booking an appointment with a doctor on this platform.
            4. Keep answers concise (3-5 sentences) and in plain, friendly language.
            """;

    public chatResponse getChatResponse(String userMessage) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> requestBody = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "system", "content", SYSTEM_PROMPT),
                        Map.of("role", "user", "content", userMessage)
                ),
                "temperature", 0.5,
                "max_tokens", 400
        );

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            String rawResponse = restTemplate.postForObject(apiUrl, requestEntity, String.class);
            return parseReply(rawResponse);
        } catch (RestClientException ex) {
            throw new ChatbotServiceException(
                    "AI assistant is temporarily unavailable. Please try again in a moment.", ex);
        }
    }

    private chatResponse parseReply(String rawResponse) {
        try {
            JsonNode root = objectMapper.readTree(rawResponse);
            String reply = root
                    .path("choices").get(0)
                    .path("message").path("content")
                    .asText();
            return new chatResponse(reply.trim());
        } catch (Exception ex) {
            throw new ChatbotServiceException("Could not read AI assistant's response.", ex);
        }
    }
}