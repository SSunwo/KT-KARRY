package com.kt.karry_backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;

@Service
public class OpenAIService {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.model}")
    private String model;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";

    public String getRecommendation(List<Map<String, Object>> factData, List<Map<String, Object>> shipments) {
        String prompt = """
            You are an AI assistant helping with shipment recommendations.
            Based on FactTable data and shipment details, provide a ranking of shipments.
            Output a JSON list of shipment IDs in recommended order.
            """;

        prompt += "\n### Fact Data:\n" + factData.toString();
        prompt += "\n### Shipments:\n" + shipments.toString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> request = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "system", "content", prompt),
                        Map.of("role", "user", "content", "Provide recommended shipments in JSON format.")
                ),
                "response_format", "json",
                "max_tokens", 500
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
        ResponseEntity<Map> response = restTemplate.exchange(OPENAI_URL, HttpMethod.POST, entity, Map.class);

        if (response.getBody() != null) {
            return response.getBody().get("choices").toString();
        }
        return "[]";
    }
}