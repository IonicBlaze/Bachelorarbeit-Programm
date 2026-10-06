package ch.hofmann.refinement_notes_consolidation.configuration;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class ModelsConfiguration {

    @Bean
    public OpenAiChatModel openAiChatModel(@Value("${OPENAI_API_KEY}") String apiKey) {
        return OpenAiChatModel.builder()
                .options(OpenAiChatOptions.builder()
                        .apiKey(apiKey)
                        .model("gpt-6-astra")
                        .reasoningEffort("low")
                        .build())
                .build();
    }

    @Bean
    public OpenAiChatModel qwenChatModel() {
        return OpenAiChatModel.builder()
                .options(OpenAiChatOptions.builder()
                        .baseUrl("http://192.168.250.1:1234/v1")
                        .apiKey("doesnt-matter")
                        .model("qwen/qwen3.8-27b")
                        .extraBody(Map.of("reasoning_effort", "low"))
                        .build())
                .build();
    }

    @Bean
    public ChatClient openAiChatClient(@Qualifier("openAiChatModel") OpenAiChatModel model) {
        return ChatClient.builder(model).build();
    }

    @Bean
    public ChatClient qwenChatClient(@Qualifier("qwenChatModel") OpenAiChatModel model) {
        return ChatClient.builder(model).build();
    }
}
