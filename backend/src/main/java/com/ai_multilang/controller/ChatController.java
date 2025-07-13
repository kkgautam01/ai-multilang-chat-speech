package com.ai_multilang.controller;

import com.ai_multilang.model.ChatRequest;
import com.ai_multilang.model.ChatResponse;
import com.ai_multilang.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @CrossOrigin(origins = "*")
    @PostMapping
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        String reply = chatService.getChatReply(request.getAiProvider(), request.getPrompt());
        return ResponseEntity.ok(new ChatResponse(reply));
    }

}
