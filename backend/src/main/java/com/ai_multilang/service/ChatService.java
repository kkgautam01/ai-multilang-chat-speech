package com.ai_multilang.service;

import com.ai_multilang.factory.AIChatStrategyFactory;
import com.ai_multilang.strategy.AIChatStrategy;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final AIChatStrategyFactory factory;

    public ChatService(AIChatStrategyFactory factory) {
        this.factory = factory;
    }

    public String getChatReply(String aiProvider, String message) {
        AIChatStrategy strategy = factory.getStrategy(aiProvider);
        return strategy.getResponse(message);
    }
}
