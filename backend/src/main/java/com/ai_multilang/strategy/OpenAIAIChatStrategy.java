package com.ai_multilang.strategy;
import com.ai_multilang.utils.Constants;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OpenAIAIChatStrategy implements AIChatStrategy {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String getResponse(String message) {
        // Build request body
        Map<String, Object> body = new HashMap<>();
        body.put("model", Constants.MODEL);
        List<Map<String, String>> messages = Collections.singletonList(
                Map.of("role", "user", "content", message)
        );
        body.put("messages", messages);
        body.put("temperature", 0.7);

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(Constants.OPENAI_API_KEY);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(Constants.OPENAI_URL, HttpMethod.POST, entity, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map<String, Object> messageObj = (Map<String, Object>) choices.get(0).get("message");
                    return messageObj.get("content").toString().trim();
                }
            }
            return "No valid response from OpenAI.";
        } catch (Exception e) {
            return "Error calling OpenAI: " + e.getMessage();
        }
    }
}
