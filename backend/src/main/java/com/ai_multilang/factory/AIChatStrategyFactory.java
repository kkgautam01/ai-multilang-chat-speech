package com.ai_multilang.factory;

import com.ai_multilang.strategy.AIChatStrategy;
import com.ai_multilang.strategy.LocalAIChatStrategy;
import com.ai_multilang.strategy.OpenAIAIChatStrategy;
import org.springframework.stereotype.Component;

@Component
public class AIChatStrategyFactory {

    public AIChatStrategy getStrategy(String aiProvider) {
        if ("openai".equalsIgnoreCase(aiProvider)) {
            return new OpenAIAIChatStrategy();
        } else {
            return new LocalAIChatStrategy();
        }
    }
}
