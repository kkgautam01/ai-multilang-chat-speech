package com.ai_multilang.strategy;

import com.ai_multilang.utils.Constants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
@Component
public class LocalAIChatStrategy implements AIChatStrategy {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String getResponse(String prompt) {
        RestTemplate restTemplate = new RestTemplate();

        try {
            String requestBody = """
                    {
                      "model": "gemma:2b",
                      "prompt": "%s"
                    }
                    """.formatted(prompt);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(Constants.OLLAMA_API_URL, entity, String.class);

            return response.getBody();
        }catch (Exception e){
            return "Please ask again, or format the question";
        }

    }
}
