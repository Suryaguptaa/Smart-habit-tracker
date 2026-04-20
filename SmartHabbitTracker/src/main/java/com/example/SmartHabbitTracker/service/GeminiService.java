package com.example.SmartHabbitTracker.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    @Value("${groq.api.key}")
    private String apiKey;

    private static final String GROQ_URL = "https://api.groq.com/openai/v1/chat/completions";

    public String getHealthTip(String habitName, int streak) {
        try {
            String prompt = String.format(
                    "A user is tracking a habit '%s' with a %d-day streak. Give a short motivational health tip (2 sentences max).",
                    habitName, streak
            );

            Map<String, Object> message = new HashMap<>();
            message.put("role", "user");
            message.put("content", prompt);

            Map<String, Object> body = new HashMap<>();
            body.put("model", "llama-3.1-8b-instant");
            body.put("messages", List.of(message));
            body.put("max_tokens", 100);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> response = restTemplate.exchange(GROQ_URL, HttpMethod.POST, entity, Map.class);

            Map res = response.getBody();
            if (res == null || !res.containsKey("choices")) {
                return "Could not get an AI response at this time.";
            }

            List choices = (List) res.get("choices");
            if (choices == null || choices.isEmpty()) {
                return "No suggestions available right now.";
            }

            Map choice = (Map) choices.get(0);
            Map msg = (Map) choice.get("message");
            return (String) msg.get("content");

        } catch (HttpClientErrorException e) {
            System.out.println("[AIService] HTTP error " + e.getStatusCode() + " | Body: " + e.getResponseBodyAsString());
            return "AI error " + e.getStatusCode().value() + ": " + e.getStatusText();
        } catch (Exception e) {
            System.out.println("[AIService] Unexpected error: " + e.getMessage());
            return "Something went wrong fetching the AI tip.";
        }
    }
}